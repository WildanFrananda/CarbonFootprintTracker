package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.UpdateTargetRequest
import dev.frananda.carbonfootprinttracker.data.remote.UserApi
import dev.frananda.carbonfootprinttracker.data.remote.UserProfileDto
import jakarta.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUserProfile(): Result<UserProfileDto> {
        return try {
            val response = userApi.getUserProfile()
            if (response.status == "success" && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch user profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTarget(targetKg: Double): Result<UserProfileDto> {
        return try {
            val response = userApi.updateTarget(UpdateTargetRequest(targetKg))
            if (response.status == "success" && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update target"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}