package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.data.remote.DashboardApi
import dev.frananda.carbonfootprinttracker.data.remote.HeatmapEntryDto
import dev.frananda.carbonfootprinttracker.data.remote.InsightApi
import dev.frananda.carbonfootprinttracker.data.remote.InsightDto
import dev.frananda.carbonfootprinttracker.data.remote.WeeklyDashboardDto
import dev.frananda.carbonfootprinttracker.domain.model.CategoryEmissionModel
import dev.frananda.carbonfootprinttracker.domain.model.DailyDashboardModel
import dev.frananda.carbonfootprinttracker.domain.model.HeatmapModel
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApi,
    private val insightApi: InsightApi
) : DashboardRepository {
    override suspend fun getDailyDashboard(date: String): Result<DailyDashboardModel> {
        return try {
            val response = dashboardApi.getDailyDashboard(date)
            if (response.status == "success" && response.data != null) {
                val dto = response.data

                val mappedData = DailyDashboardModel(
                    total_emission = dto.total_emission,
                    is_green_day = dto.is_green_day,
                    breakdown = listOf(
                        CategoryEmissionModel(
                            "transport",
                            dto.transport_kg
                        ),
                        CategoryEmissionModel("food", dto.food_kg),
                        CategoryEmissionModel("energy", dto.energy_kg),
                        CategoryEmissionModel("shopping", dto.shopping_kg)
                    ).filter { it.amount > 0 }
                )
                Result.success(mappedData)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch daily dashboard"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
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
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override suspend fun getWeeklyDashboard(endDate: String): Result<WeeklyDashboardDto> {
        return try {
            val response = dashboardApi.getWeeklyDashboard(endDate)
            if (response.status == "success" && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch weekly dashboard"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override suspend fun getYearlyHeatmap(year: Int): Result<List<HeatmapModel>> {
        return try {
            val response = dashboardApi.getYearlyHeatmap(year)
            if (response.status == "success" && response.data != null) {
                val mappedList = response.data.map { dto ->
                    val emission = dto.total_emission
                    HeatmapModel(
                        date = dto.date,
                        total_emission = emission,
                        is_green_day = emission < 10
                    )
                }
                Result.success(mappedList)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch heatmap"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }
}