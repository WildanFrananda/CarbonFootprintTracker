package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

@Serializable
data class UserProfileDto(
    val email: String,
    val display_name: String,
    val daily_target_kg: Double
)

@Serializable
data class UpdateTargetRequest(
    val daily_target_kg: Double
)

interface UserApi {
    @GET("/api/user/profile")
    suspend fun getUserProfile(): BaseResponse<UserProfileDto>

    @PUT("/api/user/target")
    suspend fun updateTarget(@Body request: UpdateTargetRequest): BaseResponse<UserProfileDto>
}