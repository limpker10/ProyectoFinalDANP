package com.unsa.edu.proyectofinaldanp.mqtt

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import software.amazon.awssdk.crt.CRT
import software.amazon.awssdk.crt.mqtt.MqttClientConnection
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents
import software.amazon.awssdk.crt.mqtt.MqttMessage
import software.amazon.awssdk.crt.mqtt.QualityOfService
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.UUID

class MqttConnection(private val context: Context) {
    private val endPoint = "a2axh58ph1yz86-ats.iot.us-east-1.amazonaws.com"
    private val resourceNames = listOf("AmazonRCA1.pem", "certificate.pem.crt", "private.pem.key")

    private val resourceMap: HashMap<String, String> by lazy { createResourceMap() }
    private val mqttClientConnection: MqttClientConnection by lazy { createMqttClientConnection() }

    private val mqttClientCallbacks = object : MqttClientConnectionEvents {
        override fun onConnectionInterrupted(errorCode: Int) {
            if (errorCode != 0) {
                Log.i("AWS IOT", "Connection interrupted: $errorCode: " + CRT.awsErrorString(errorCode))
            }
        }

        override fun onConnectionResumed(sessionPresent: Boolean) {
            Log.i("AWS IOT", "Connection resumed: " + if (sessionPresent) "existing session" else "clean session")
        }
    }

    private fun copyResourceToCache(resourceName: String): String {
        context.resources.assets.open(resourceName).use { res ->
            val cachedName = "${context.externalCacheDir}/$resourceName"
            FileOutputStream(cachedName).use { cachedRes ->
                res.copyTo(cachedRes)
            }
            return cachedName
        }
    }

    private fun createResourceMap(): HashMap<String, String> {
        val resourceMap = HashMap<String, String>()
        for (resourceName in resourceNames) {
            resourceMap[resourceName] = copyResourceToCache(resourceName)
        }
        return resourceMap
    }

    private fun createMqttClientConnection(): MqttClientConnection {
        val builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(
            resourceMap["certificate.pem.crt"], resourceMap["private.pem.key"]
        )

        builder.withConnectionEventCallbacks(mqttClientCallbacks)
            .withClientId("iotconsole-${UUID.randomUUID()}")
            .withEndpoint(endPoint)
            .withPort(8883)
            .withCleanSession(true)
            .withProtocolOperationTimeoutMs(60000)

        val connection = builder.build()
        val connected = connection.connect()
        try {
            val sessionPresent = connected.get()
            Log.i("AWS IOT", "Connected to ${if (sessionPresent) "new" else "existing"} session!")
        } catch (ex: Exception) {
            Log.i("AWS IOT", "Exception occurred during connect: $ex")
        }

        return connection
    }

    fun publishMessage(message: String, topic: String, qos: Int) {
        val mqttMessage = MqttMessage(topic, message.toByteArray(), QualityOfService.AT_LEAST_ONCE, false)
        val publish = mqttClientConnection.publish(mqttMessage)
        try {
            publish.get()
        } catch (ex: Exception) {
            Log.i("AWS IOT", "Exception occurred during publish: $ex")
        }
    }

    fun subscribeToTopic(topic: String, qos: Int, sensorDataTemperature: SensorDataTemperature) {
        val subscribeFuture = mqttClientConnection.subscribe(topic, QualityOfService.AT_MOST_ONCE)
        subscribeFuture.whenComplete { _, throwable ->
            if (throwable == null) {
                Log.i("MqttConnection", "Subscribed to topic: $topic")
                mqttClientConnection.subscribe("esp8266/pub", QualityOfService.AT_MOST_ONCE) { message ->
                    val payload = String(message.payload, StandardCharsets.UTF_8)
                    Log.i("MqttConnection", "Received message: $payload")
                    try {
                        val json = JSONObject(payload)
                        val humidity = json.getDouble("humidity")
                        val temperature = json.getDouble("temperature")
                        sensorDataTemperature.updateTemperature(temperature)
                    } catch (e: JSONException) {
                        Log.e("MqttConnection", "Error parsing JSON: $e")
                    }
                }
            } else {
                Log.e("MqttConnection", "Failed to subscribe to topic: $topic", throwable)
            }
        }
    }
}
