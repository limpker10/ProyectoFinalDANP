package com.unsa.edu.proyectofinaldanp.mqtt

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SensorDataTemperature {

    val temperature: MutableState<Double> = mutableStateOf(0.0)

    fun updateTemperature(temperatureValue: Double) {
        temperature.value = temperatureValue
    }
}

class SensorDataViewModel : ViewModel() {
    val sensorDataTemperature = SensorDataTemperature()
}