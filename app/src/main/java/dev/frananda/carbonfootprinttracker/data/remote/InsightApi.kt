package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET

@Serializable
data class InsightDto(
    val dominant_category: String,
    val message: String,
    val ai_insight: String
)

interface InsightApi {
    @GET("/api/insights/recommendations")
    suspend fun getRecommendations(): BaseResponse<InsightDto>
}