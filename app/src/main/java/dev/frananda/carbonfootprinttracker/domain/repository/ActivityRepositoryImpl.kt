package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.ActivityApi
import dev.frananda.carbonfootprinttracker.data.remote.ActivityRequest
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val activityApi: ActivityApi
) : ActivityRepository {
    override suspend fun logActivity(request: ActivityRequest): Result<Unit> {
        return try {
            val response = activityApi.logActivity(request)
            if (response.status == "success") {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to log activity"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}