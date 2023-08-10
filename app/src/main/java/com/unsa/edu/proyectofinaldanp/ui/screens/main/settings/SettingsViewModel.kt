package com.unsa.edu.proyectofinaldanp.ui.screens.main.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unsa.edu.proyectofinaldanp.ui.pagination.models.ApiService
import com.unsa.edu.proyectofinaldanp.ui.pagination.models.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsViewModel : ViewModel() {

    private val _dataItems: MutableLiveData<List<Pair<Int, Double>>> = MutableLiveData()
    val dataItems: LiveData<List<Pair<Int, Double>>> get() = _dataItems

    init {
        fetchLatestDataItems(20)
    }
    // Function to fetch the latest data items
    fun fetchLatestDataItems(count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getLatestDataItems(count)
            _dataItems.postValue(result)
        }
    }

    private suspend fun getLatestDataItems(count: Int): List<Pair<Int, Double>> {
        return try {
            ApiService.getLatestDataItems(count)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

