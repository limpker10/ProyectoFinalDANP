package com.unsa.edu.proyectofinaldanp.ui.screens.main.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dnap_finalproject.components.Indicator
import com.example.dnap_finalproject.components.LineChart
import com.example.dnap_finalproject.components.ToggleButton
import com.example.dnap_finalproject.mqtt.MqttConnection
import com.unsa.edu.proyectofinaldanp.mqtt.SensorDataViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen() {

    val context = LocalContext.current
    val mqttConnection = remember { MqttConnection(context) }
    var isOn by remember { mutableStateOf(false) }
    val sensorDataViewModel: SensorDataViewModel = viewModel()
    val temperature = sensorDataViewModel.sensorDataTemperature.temperature.value

    LaunchedEffect(Unit) {
        mqttConnection.subscribeToTopic("esp8266/pub", 1, sensorDataViewModel.sensorDataTemperature)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        //val temperature = mqttObserver.temperature

        Indicator(indicatorValue = temperature.toInt() )
        Spacer(modifier = Modifier.height(16.dp))
        LineChart(yPoints = listOf(
            219f, 22f, 23f, 220f, 130f, 20f, 50f, 100f, 201f, 1f
        ),)
        Spacer(modifier = Modifier.height(16.dp))
        ToggleButton(
            onButtonClick = {
                val message = if (isOn) "{ \"message\": \"apagar_led\" }" else "{ \"message\": \"encender_led\" }"
                val topic = "esp8266/pub"
                val qos = 1
                mqttConnection.publishMessage(message, topic, qos)
                isOn = !isOn
            },
            isOn = isOn
        )
    }
}