package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.ActivityRequest

interface ActivityRepository {
    suspend fun logActivity(request: ActivityRequest): Result<Unit>
}