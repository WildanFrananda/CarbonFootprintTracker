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
    val total_emission: Double,
    val is_green_day: Boolean,
    val breakdown: List<CategoryEmission>
)

interface DashboardApi {
    @GET("api/dashboard/daily")
    suspend fun getDailyDashboard(@Query("date") date: String): BaseResponse<DailyDashboardDto>
}