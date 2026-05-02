package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.InsightDto
import dev.frananda.carbonfootprinttracker.data.remote.WeeklyDashboardDto
import dev.frananda.carbonfootprinttracker.domain.model.DailyDashboardModel
import dev.frananda.carbonfootprinttracker.domain.model.HeatmapModel

interface DashboardRepository {
    suspend fun getDailyDashboard(date: String): Result<DailyDashboardModel>
    suspend fun getInsights(): Result<InsightDto>
    suspend fun getWeeklyDashboard(endDate: String): Result<WeeklyDashboardDto>
    suspend fun getYearlyHeatmap(year: Int): Result<List<HeatmapModel>>
}