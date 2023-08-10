package com.unsa.edu.proyectofinaldanp.ui.pagination.models

data class TemperatureData(
    val temperature: Double,
    val UnitTemperature: String,
    val comment: String
)

data class DataItem(
    val id: Int,
    val timestamp: String,
    val temperature: String,
    val UnitTemperature: String
)