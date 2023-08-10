package com.example.dnap_finalproject.mqtt

import android.content.Context
import android.util.Log
import com.unsa.edu.proyectofinaldanp.mqtt.SensorDataTemperature
import org.json.JSONObject
import software.amazon.awssdk.crt.CRT
import software.amazon.awssdk.crt.mqtt.MqttClientConnection
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents
import software.amazon.awssdk.crt.mqtt.MqttMessage
import software.amazon.awssdk.crt.mqtt.QualityOfService
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

class MqttConnection(private val context: Context) {
    private val endPoint = "a2axh58ph1yz86-ats.iot.us-east-1.amazonaws.com"
    private val rootCa = "Amazon.pem"
    private val cert = "certificate.pem.crt"
    private val privateKey = "private.pem.key"

    private val resourceMap: HashMap<String, String> by lazy { createResourceMap() }
    private val mqttClientConnection: MqttClientConnection by lazy { createMqttClientConnection() }

    private fun createResourceMap(): HashMap<String, String> {
        val resourceNames = listOf(rootCa, cert, privateKey)
        val resourceMap = HashMap<String, String>()
        for (resourceName in resourceNames) {
            context.resources.assets.open(resourceName).use { res ->
                val cachedName = "${context.externalCacheDir}/${resourceName}"
                FileOutputStream(cachedName).use { cachedRes ->
                    res.copyTo(cachedRes)
                }
                resourceMap[resourceName] = cachedName
            }
        }
        return resourceMap
    }

    private fun createMqttClientConnection(): MqttClientConnection {
        val builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(
            resourceMap[cert], resourceMap[privateKey]
        )

        val callbacks = object : MqttClientConnectionEvents {
            override fun onConnectionInterrupted(errorCode: Int) {
                if (errorCode != 0) {
                    Log.i("AWS IOT", "Connection interrupted: $errorCode: " + CRT.awsErrorString(errorCode))
                }
            }

            override fun onConnectionResumed(sessionPresent: Boolean) {
                Log.i("AWS IOT", "Connection resumed: " + if (sessionPresent) "existing session" else "clean session")
            }
        }

        builder
            .withConnectionEventCallbacks(callbacks)
            .withClientId("iotconsole-eeea290f-5d8d-4c76-aed9-f39d38d78f83")
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
        val publish = mqttClientConnection.publish( MqttMessage(topic, message.toByteArray(), QualityOfService.AT_LEAST_ONCE, false))
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
                mqttClientConnection.subscribe("esp8266/sub", QualityOfService.AT_MOST_ONCE){ message ->
                        val payload = String(message.payload, StandardCharsets.UTF_8)
                        Log.i("MqttConnection", "Received message: $payload")
                        // Parsear JSON y obtener los valores de humedad y temperatura
                        val json = JSONObject(payload)
                        val humidity = json.getDouble("humidity")
                        val temperature = json.getDouble("temperature")
                        // Actualizar los valores de humedad y temperatura
                        sensorDataTemperature.updateTemperature(temperature)
                }
            } else {
                Log.e("MqttConnection", "Failed to subscribe to topic: $topic", throwable)
            }
        }
    }
}