package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.DailyDashboardDto
import dev.frananda.carbonfootprinttracker.data.remote.InsightDto


interface DashboardRepository {
    suspend fun getDailyDashboard(date: String): Result<DailyDashboardDto>
    suspend fun getInsights(): Result<InsightDto>
}