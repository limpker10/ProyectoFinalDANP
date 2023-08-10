package com.unsa.edu.proyectofinaldanp.ui.screens.Comand

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unsa.edu.proyectofinaldanp.R
import com.unsa.edu.proyectofinaldanp.components.ToggleButton
import com.unsa.edu.proyectofinaldanp.mqtt.MqttConnection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandScreen() {
    val context = LocalContext.current
    val mqttConnection = remember { MqttConnection(context) }

    Box(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SliderWithLock { sliderPosition ->
                // Aquí puedes realizar cualquier acción adicional con sliderPosition si es necesario
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun SliderWithLock(enviarAWS: (Float) -> Unit) {
    var sliderPosition by remember { mutableStateOf(1f) }
    var isLocked by remember { mutableStateOf(false) }
    var on by remember { mutableStateOf(false) } // Agregamos la variable 'on'
    val context = LocalContext.current
    val mqttConnection = remember { MqttConnection(context) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(Color(0xFF9765F1)) // Color con transparencia para el efecto de vidrio
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Logo de Iot",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f)
            )

            Text(
                text = "¡Regula la temperatura!",
                style = MaterialTheme.typography.headlineLarge,

                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(26.dp))


        Text(
            text = "Regula la temperatura :",
        )
        Spacer(modifier = Modifier.height(36.dp))

        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->

                    sliderPosition = newValue


                    enviarAWS(sliderPosition)

            },
            valueRange = 0f..100f,
            modifier = Modifier
                .fillMaxWidth(),
            thumb = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.mode_fan),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
            },
            colors = SliderDefaults.colors(
                thumbColor = Color.Blue, // Color del pulgar del Slider
                activeTrackColor = Color.Red, // Color de la barra activa del Slider
                inactiveTrackColor = Color.Gray // Color de la barra inactiva del Slider
            )

        )

        Row(
            modifier = Modifier
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    isLocked = !isLocked

                    // Crear el mensaje JSON basado en el nuevo valor de bloqueo
                    val message =  "{ \"value\": \"$sliderPosition\" }"
                    val topic = "esp8266/sub"
                    val qos = 1
                    mqttConnection.publishMessage(message, topic, qos)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors()

            ) {
                Text(text = "Enviar Comando")
                Icon(
                    imageVector = if (isLocked) ImageVector.vectorResource(id = R.drawable.lock) else ImageVector.vectorResource(
                        id = R.drawable.unlocked
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}