package dev.frananda.carbonfootprinttracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import dev.frananda.carbonfootprinttracker.data.remote.WeeklyEmission

@Composable
fun WeeklyBarChart(weeklyData: List<WeeklyEmission>) {
    if (weeklyData.isEmpty()) return

    val chartEntryModel = remember(weeklyData) {
        CartesianChartModel(
            ColumnCartesianLayerModel.build {
                series(weeklyData.map { it.amount.toFloat() })
            }
        )
    }

    val bottomAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
        weeklyData.getOrNull(value.toInt())?.day ?: ""
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = CartesianValueFormatter { _, value, _ ->
                    "${value.toInt()} kg"
                }
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter
            )
        ),
        model = chartEntryModel,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}