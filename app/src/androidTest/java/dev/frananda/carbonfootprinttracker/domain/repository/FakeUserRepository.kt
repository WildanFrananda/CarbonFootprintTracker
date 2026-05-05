package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.UserProfileDto

class FakeUserRepository : UserRepository {
    override suspend fun getUserProfile(): Result<UserProfileDto> = 
        Result.success(UserProfileDto("test@mail.com", "Test User", 100.0))
        
    override suspend fun updateTarget(targetKg: Double): Result<UserProfileDto> = 
        Result.success(UserProfileDto("test@mail.com", "Test User", targetKg))
}
