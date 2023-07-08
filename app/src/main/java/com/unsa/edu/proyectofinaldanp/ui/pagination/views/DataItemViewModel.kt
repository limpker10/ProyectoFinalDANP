package com.unsa.edu.proyectofinaldanp.ui.pagination.views

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unsa.edu.proyectofinaldanp.ui.pagination.DataItemPagingSource
import com.unsa.edu.proyectofinaldanp.ui.pagination.models.DataItem
import kotlinx.coroutines.flow.Flow

class DataItemViewModel : ViewModel() {
    private val pageSize = 10 // Tamaño de página deseado

    fun getDataItems(): Flow<PagingData<DataItem>> {
        return Pager(PagingConfig(pageSize)) {
            DataItemPagingSource(pageSize)
        }.flow
    }
}