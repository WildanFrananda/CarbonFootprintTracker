package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET

@Serializable
data class InsightDto(
    val recommendations: List<String>
)

interface InsightApi {
    @GET("/api/insight/recommendations")
    suspend fun getRecommendations(): BaseResponse<InsightDto>
}