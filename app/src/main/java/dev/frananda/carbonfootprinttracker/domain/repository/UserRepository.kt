package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.UserProfileDto

interface UserRepository {
    suspend fun getUserProfile(): Result<UserProfileDto>
    suspend fun updateTarget(targetKg: Double): Result<UserProfileDto>
}