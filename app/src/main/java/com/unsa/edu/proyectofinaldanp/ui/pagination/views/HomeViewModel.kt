package com.unsa.edu.proyectofinaldanp.ui.pagination.views

import com.unsa.edu.proyectofinaldanp.ui.pagination.models.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel {

    private val _showToast = MutableStateFlow(false)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val showToast: StateFlow<Boolean> = _showToast.asStateFlow()
    fun addItem(temperature: String, unit: String, comentario: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    // Llamar a la función suspend
                    addItemSuspend(temperature.toDouble(), unit, comentario)
                }
                // La llamada se realizó con éxito
                triggerToast()

            } catch (e: Exception) {
                e.printStackTrace()
                // Ocurrió un error durante la llamada
            }
        }
    }
    private suspend fun addItemSuspend(temperature: Double, unit: String, comentario:String){
        ApiService.postLambdaFunction(temperature,unit,comentario)
    }
    fun triggerToast() {
        _showToast.value = true
    }
}