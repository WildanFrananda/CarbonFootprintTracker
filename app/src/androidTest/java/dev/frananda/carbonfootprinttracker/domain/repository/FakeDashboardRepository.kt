package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.InsightDto
import dev.frananda.carbonfootprinttracker.data.remote.WeeklyDashboardDto
import dev.frananda.carbonfootprinttracker.domain.model.DailyDashboardModel
import dev.frananda.carbonfootprinttracker.domain.model.HeatmapModel

class FakeDashboardRepository : DashboardRepository {
    override suspend fun getDailyDashboard(date: String): Result<DailyDashboardModel> = 
        Result.success(DailyDashboardModel(0.0, true, emptyList()))

    override suspend fun getInsights(): Result<InsightDto> = 
        Result.success(InsightDto("transport", "Good job", "Keep it up!"))

    override suspend fun getWeeklyDashboard(endDate: String): Result<WeeklyDashboardDto> = 
        Result.failure(Exception("Not implemented"))

    override suspend fun getYearlyHeatmap(year: Int): Result<List<HeatmapModel>> = 
        Result.success(emptyList())
}
