package dev.frananda.carbonfootprinttracker.domain.model

data class CategoryEmissionModel(
    val category: String,
    val amount: Double
)

data class DailyDashboardModel(
    val total_emission: Double,
    val is_green_day: Boolean,
    val breakdown: List<CategoryEmissionModel>
)

data class HeatmapModel(
    val date: String,
    val total_emission: Double,
    val is_green_day: Boolean
)
