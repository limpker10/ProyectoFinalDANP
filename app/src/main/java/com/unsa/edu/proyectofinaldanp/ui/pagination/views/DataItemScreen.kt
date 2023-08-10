package com.unsa.edu.proyectofinaldanp.ui.pagination.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.unsa.edu.proyectofinaldanp.R
import com.unsa.edu.proyectofinaldanp.ui.pagination.models.DataItem
import kotlinx.coroutines.flow.Flow


@Composable
fun DataItemScreen(viewModel: DataItemViewModel) {
    val dataItems: Flow<PagingData<DataItem>> = viewModel.getDataItems()

    val lazyDataItems: LazyPagingItems<DataItem> = dataItems.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyDataItems) { dataItem ->
            if (dataItem != null) {
                DataItemCard(dataItem)
            }
        }
        lazyDataItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val errorMessage = (loadState.refresh as LoadState.Error).error.message
                    item {
                        Text(
                            text = "Error: $errorMessage",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                        )
                    }
                }
                loadState.append is LoadState.NotLoading -> {

                    item {
                        Text(
                            text = "No hay mas registros disponibles",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,

                            )
                    }

                }
            }
        }
    }
}
@Composable
fun DataItemCard(dataItem: DataItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_thermostat_24),
                    contentDescription = "Favorite",
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "ID: "+ dataItem.id.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row() {

                    Text(
                        text = dataItem.temperature,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dataItem.UnitTemperature,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dataItem.timestamp,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

        }
    }
}