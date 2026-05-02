package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class ActivityRequest(
    val category: String,
    val subcategory: String,
    val quantity: Double,
    val date: String
)

@Serializable
data class ActivityResponseDto(
    val id: Int? = null,
    val category: String,
    val subcategory: String,
    val quantity: Double,
    val date: String,
    val calculate_emission_kg: Double
)

@Serializable
data class EmissionFactorDto(
    val category: String,
    val subcategory: String,
    val unit: String
)

interface ActivityApi {
    @POST("/api/activities")
    suspend fun logActivity(@Body request: ActivityRequest): BaseResponse<Unit>

    @GET("/api/activities")
    suspend fun getActivities(@Query("date") date: String): BaseResponse<List<ActivityResponseDto>>

    @DELETE("/api/activities/{id}")
    suspend fun deleteActivity(@Path("id") id: Int): BaseResponse<Unit>

    @GET("/api/activities/factors")
    suspend fun getEmissionFactors(): BaseResponse<List<EmissionFactorDto>>
}