package com.unsa.edu.proyectofinaldanp.ui.pagination.models

data class TemperatureData(
    val temperature: Double,
    val unit: String,
    val comment: String
)

data class DataItem(
    val id: Int,
    val timestamp: String,
    val temperature: String,
    val unidad: String
)