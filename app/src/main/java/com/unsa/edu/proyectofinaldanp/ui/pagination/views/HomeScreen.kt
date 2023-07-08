package com.unsa.edu.proyectofinaldanp.ui.pagination.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController
){
    val showToast by homeViewModel.showToast.collectAsState()
    ToastOnBooleanChange(
        showToast = showToast,
        message = "¡Se agrego un nuevo Parametro!"
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var valor by remember { mutableStateOf("") }
        var grado by remember { mutableStateOf("") }
        var comentario by remember { mutableStateOf("") }

        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            label = { Text("Temperatura a registrar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = grado,
            onValueChange = { grado = it },
            label = { Text("Escala de Temperatura") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = comentario,
            onValueChange = { comentario = it },
            label = { Text("Comentario") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                homeViewModel.addItem(valor, grado,comentario)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Guardar")
        }

        Button(
            onClick = {
                navController.navigate("TempList")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Ver Lista")
        }
    }
}

@Composable
fun ToastOnBooleanChange(
    showToast: Boolean,
    message: String
) {
    if (showToast) {
        Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
    }
}