package com.example.dnap_finalproject.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.dp


@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    yPoints: List<Float>,
    graphColor: Color = MaterialTheme.colors.primary
) {
    val spacing = 0f
    val minY = yPoints.minOrNull() ?: 0f
    val maxY = yPoints.maxOrNull() ?: 100f

    val normalizedYPoints = yPoints.map { y ->
        // Normalize y values to fit between 0 and 100
        val normalizedY = ((y - minY) / (maxY - minY)) * 200f
        // Flip the y-axis (because y increases downwards in Canvas)
        200f - normalizedY
    }

    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(all = 16.dp)
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            val spacePerHour = ((size.width - spacing) / (normalizedYPoints.size - 1))

            val normX = mutableListOf<Float>()
            val normY = mutableListOf<Float>()

            val strokePath = Path().apply {

                for (i in normalizedYPoints.indices) {

                    val currentX = spacing + i * spacePerHour
                    val currentY = normalizedYPoints[i]

                    if (i == 0) {
                        moveTo(currentX, currentY)
                    } else {
                        val previousX = spacing + (i - 1) * spacePerHour
                        val conX1 = (previousX + currentX) / 2f
                        val conX2 = (previousX + currentX) / 2f
                        val conY1 = normY[i - 1]
                        val conY2 = currentY

                        cubicTo(
                            x1 = conX1,
                            y1 = conY1,
                            x2 = conX2,
                            y2 = conY2,
                            x3 = currentX,
                            y3 = currentY
                        )
                    }

                    // Circle dot points
                    normX.add(currentX)
                    normY.add(currentY)
                }
            }

            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round
                )

            )

            val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
                lineTo(size.width, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        graphColor.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    endY = size.height - spacing
                )
            )

            (normX.indices).forEach {
                drawCircle(
                    graphColor,
                    radius = 3.dp.toPx(),
                    center = Offset(normX[it], normY[it])
                )
            }
        }
    }
}

