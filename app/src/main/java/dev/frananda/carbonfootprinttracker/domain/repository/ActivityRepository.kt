package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.ActivityRequest
import dev.frananda.carbonfootprinttracker.data.remote.EmissionFactorDto

interface ActivityRepository {
    suspend fun logActivity(request: ActivityRequest): Result<Unit>

    suspend fun getEmissionFactors(): Result<List<EmissionFactorDto>>
}