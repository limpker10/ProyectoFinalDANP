package com.example.dnap_finalproject.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ToggleButton(
    onButtonClick: () -> Unit,
    isOn: Boolean
) {

    val toggleState = remember { mutableStateOf(isOn) }

    IconButton(
        onClick = {
            toggleState.value = !toggleState.value
            onButtonClick()
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
            }
        },
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(if (toggleState.value) MaterialTheme.colors.primary else Color.Gray),
    ) {
        Icon(
            imageVector = if (toggleState.value) Icons.Default.Check else Icons.Default.Check,
            contentDescription = "Toggle Button",
            tint = Color.White
        )
    }
}
