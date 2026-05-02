package dev.frananda.carbonfootprinttracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.frananda.carbonfootprinttracker.domain.model.HeatmapModel

@Composable
fun GithubHeatmap(entries: List<HeatmapModel>): Unit {
    val entriesMap = entries.associateBy { it.date }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val columns = 52
            val rows = 7
            val spacing = 4.dp.toPx()

            val cellSize = (size.width - (spacing * (columns - 1))) / columns

            val emptyColor = Color(0xFFE0E0E0)
            val lightGreen = Color(0xFFA5D6A7)
            val midGreen = Color(0xFF4CAF50)
            val darkGreen = Color(0xFF2E7D32)
            val redColor = Color(0xFFEF5350)

            for (col in 0 until columns) {
                for (row in 0 until rows) {
                    val x = col * (cellSize + spacing)
                    val y = row * (cellSize + spacing)

                    // TODO: The actual mapping algorithm should match (col, row) with dates in the current year.
                    // For a visual demonstration, let's take linear data from list entries.
                    val index = col * rows + row
                    val entry = entries.getOrNull(index)

                    val cellColor = if (entry == null || entry.total_emission == 0.0) {
                        emptyColor
                    } else if (!entry.is_green_day) {
                        redColor
                    } else {
                        when {
                            entry.total_emission < 5.0 -> darkGreen
                            entry.total_emission < 15.0 -> midGreen
                            else -> lightGreen
                        }
                    }

                    drawRoundRect(
                        color = cellColor,
                        topLeft = Offset(x, y),
                        size = Size(cellSize, cellSize),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )
                }
            }
        }
    }
}