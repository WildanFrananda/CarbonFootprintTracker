package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.DailyDashboardDto
import dev.frananda.carbonfootprinttracker.data.remote.DashboardApi
import dev.frananda.carbonfootprinttracker.data.remote.InsightApi
import dev.frananda.carbonfootprinttracker.data.remote.InsightDto
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApi,
    private val insightApi: InsightApi
) : DashboardRepository {
    override suspend fun getDailyDashboard(date: String): Result<DailyDashboardDto> {
        return try {
            val response = dashboardApi.getDailyDashboard(date)
            if (response.status == "success" && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch daily dashboard"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getInsights(): Result<InsightDto> {
        return try {
            val response = insightApi.getRecommendations()
            if (response.status == "success" && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch insights"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}