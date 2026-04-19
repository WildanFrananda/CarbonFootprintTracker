package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
data class ActivityRequest(
    val category: String,
    val subcategory: String,
    val quantity: Double,
    val date: String
)

interface ActivityApi {
    @POST("/api/activities")
    suspend fun logActivity(@Body request: ActivityRequest): BaseResponse<Unit>
}