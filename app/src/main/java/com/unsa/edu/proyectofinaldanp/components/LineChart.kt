package com.unsa.edu.proyectofinaldanp.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unsa.edu.proyectofinaldanp.ui.screens.main.settings.SettingsViewModel
import kotlinx.coroutines.delay
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun LineChart(
    viewModel: SettingsViewModel,
    data: LiveData<List<Pair<Int, Double>>>,
    modifier: Modifier = Modifier,
    textColor: Color
) {
    val spacing = 150f
    val graphColor = Color.Cyan
    val transparentGraphColor = graphColor.copy(alpha = 0.5f)
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = with(density) { 12.sp.toPx() }
            color = textColor.toArgb()
        }
    }

    val chartData by data.observeAsState(emptyList())
    val upperValue = 50.0
    val lowerValue = 0.0

    LaunchedEffect(true) {
        val interval = 7000L // 2 seconds
        while (true) {
            viewModel.fetchLatestDataItems(20)
            delay(interval)
        }
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / chartData.size

        // Draw X-axis labels
        chartData.indices.step(2).forEach { i ->
            val hour = chartData[i].first
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + i * spacePerHour,
                    size.height,
                    textPaint

                )
            }
        }

        // Draw Y-axis labels with decimal values
        val decimalStep = (upperValue - lowerValue) / 3.0
        val decimalRange = generateSequence(lowerValue) { it + decimalStep }.take(4)
        decimalRange.forEachIndexed { i, value ->
            drawContext.canvas.nativeCanvas.apply {
                val yLabel = String.format("%.2f", value)
                drawText(
                    yLabel,
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }

        // Draw the line chart
        val strokePath = Path().apply {
            val height = size.height
            chartData.indices.forEach { i ->
                val info = chartData[i]
                val ratio = (info.second - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (ratio * height).toFloat()

                if (i == 0) {
                    moveTo(x1, y1)
                } else {
                    lineTo(x1, y1)
                }
            }
        }

        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(size.width - spacePerHour, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent

                ),
                endY = size.height - spacing
            )
        )
    }

}
