package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.ActivityRequest
import dev.frananda.carbonfootprinttracker.data.remote.EmissionFactorDto

class FakeActivityRepository : ActivityRepository {
    override suspend fun logActivity(request: ActivityRequest): Result<Unit> = Result.success(Unit)
    override suspend fun getEmissionFactors(): Result<List<EmissionFactorDto>> = Result.success(emptyList())
}
