package com.unsa.edu.proyectofinaldanp.ui.screens.main.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unsa.edu.proyectofinaldanp.components.Indicator
import com.unsa.edu.proyectofinaldanp.mqtt.MqttConnection
import com.unsa.edu.proyectofinaldanp.components.LineChart
import com.unsa.edu.proyectofinaldanp.mqtt.SensorDataViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val context = LocalContext.current
    val mqttConnection = remember { MqttConnection(context) }
    val sensorDataViewModel: SensorDataViewModel = viewModel()
    val temperature = sensorDataViewModel.sensorDataTemperature.temperature.value
    //viewModel.fetchLatestDataItems(20)
    val list = viewModel.dataItems

    LaunchedEffect(Unit) {
        mqttConnection.subscribeToTopic("esp8266/pub", 1, sensorDataViewModel.sensorDataTemperature)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 80.dp, horizontal = 42.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Indicator(indicatorValue = temperature.toFloat())
        // Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Registro Historico de datos",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        LineChart(viewModel, list, Modifier.fillMaxSize(), MaterialTheme.colorScheme.primary)
    }
}