package dev.frananda.carbonfootprinttracker.ui.components

import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.frananda.carbonfootprinttracker.domain.model.CategoryEmissionModel

@Composable
fun DonutChart(
    emissions: List<CategoryEmissionModel>,
    totalEmission: Double,
    modifier: Modifier = Modifier
): Unit {
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    val sweepAngleAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) 360f else 0f,
        animationSpec = FloatTweenSpec(duration = 1000),
        label = "DonutChartAnimation"
    )

    val colorMap = mapOf(
        "transport" to Color(0xFF2196F3),
        "food" to Color(0xFFFF9800),
        "energy" to Color(0xFFFFC107),
        "shopping" to Color(0xFF9C27B0)
    )

    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            val strokeWidth = 40f

            for (emission in emissions) {
                val sweepAngle = ((emission.amount / totalEmission).toFloat() * 350f)
                val currentSweep = sweepAngle.coerceAtMost(sweepAngleAnimation - (startAngle - 90f))

                if (currentSweep > 0) {
                    drawArc(
                        color = colorMap[emission.category.lowercase()] ?: Color.Gray,
                        startAngle = startAngle,
                        sweepAngle = currentSweep,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                    )
                }
                startAngle += sweepAngle
            }
        }

        Text(
            text = "${"%.1f".format(totalEmission)} kg",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}