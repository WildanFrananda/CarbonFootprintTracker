package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class CategoryEmission(
    val category: String,
    val amount: Double
)

@Serializable
data class DailyDashboardDto(
    val date: String,
    val total_emission: Double,
    val transport_kg: Double,
    val food_kg: Double,
    val energy_kg: Double,
    val shopping_kg: Double,
    val is_green_day: Boolean
)

@Serializable
data class WeeklyEmission(
    val day: String,
    val amount: Double
)

@Serializable
data class WeeklyDashboardDto(
    val weekly_total: Double = 0.0,
    val daily_data: List<WeeklyEmission> = emptyList()
)

@Serializable
data class HeatmapEntryDto(
    val date: String,
    val total_emission: Double
)

@Serializable
data class HeatmapDto(
    val year: Int = 2024,
    val entries: List<HeatmapEntryDto> = emptyList()
)

interface DashboardApi {
    @GET("api/dashboard/daily")
    suspend fun getDailyDashboard(@Query("date") date: String): BaseResponse<DailyDashboardDto>

    @GET("api/dashboard/weekly")
    suspend fun getWeeklyDashboard(@Query("end_date") endDate: String): BaseResponse<WeeklyDashboardDto>

    @GET("/api/dashboard/heatmap")
    suspend fun getYearlyHeatmap(@Query("year") year: Int): BaseResponse<List<HeatmapEntryDto>>
}