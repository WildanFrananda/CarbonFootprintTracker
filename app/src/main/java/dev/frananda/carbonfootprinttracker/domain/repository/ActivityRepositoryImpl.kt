package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.data.remote.ActivityApi
import dev.frananda.carbonfootprinttracker.data.remote.ActivityRequest
import dev.frananda.carbonfootprinttracker.data.remote.EmissionFactorDto
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
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override suspend fun getEmissionFactors(): Result<List<EmissionFactorDto>> {
        return try {
            val response = activityApi.getEmissionFactors()
            if (response.status == "success" && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch emission factors"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }
}