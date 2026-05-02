package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET

@Serializable
data class BadgeDto(
    val badge_type: String,
    val earned_at: String,
    val description: String
)

interface GamificationApi {
    @GET("/api/gamification/badges")
    suspend fun getBadges(): BaseResponse<List<BadgeDto>>
}