package com.unsa.edu.proyectofinaldanp.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unsa.edu.proyectofinaldanp.ui.pagination.models.ApiService
import com.unsa.edu.proyectofinaldanp.ui.pagination.models.DataItem


class DataItemPagingSource(private val pageSize: Int) : PagingSource<Int, DataItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItem> {
        return try {
            val page = params.key ?: 1
            val dataItems = ApiService.callLambdaFunction(page, pageSize)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (dataItems.size == pageSize) page + 1 else null

            LoadResult.Page(
                data = dataItems,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataItem>): Int? {
        // Utilizar la clave más cercana a la posición actual o null para cargar desde el principio
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}