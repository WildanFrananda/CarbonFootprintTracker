package dev.frananda.carbonfootprinttracker.ui.features.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.core.utils.DateUtils
import dev.frananda.carbonfootprinttracker.data.remote.WeeklyDashboardDto
import dev.frananda.carbonfootprinttracker.domain.model.HeatmapModel
import dev.frananda.carbonfootprinttracker.domain.repository.DashboardRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class AnalyticUiState(
    val isLoading: Boolean = true,
    val weeklyData: WeeklyDashboardDto? = null,
    val heatmapData: List<HeatmapModel>? = null,
    val error: String? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AnalyticUiState())
    val uiState: StateFlow<AnalyticUiState> = _uiState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    fun loadAnalyticsData(): Unit {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val todayDate = DateUtils.getCurrentDateInLocalTimezone()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            val weeklyDeferred = async { dashboardRepository.getWeeklyDashboard(todayDate) }
            val heatmapDeferred = async { dashboardRepository.getYearlyHeatmap(currentYear) }

            val weeklyResult = weeklyDeferred.await()
            val heatmapResult = heatmapDeferred.await()

            if (weeklyResult.isSuccess && heatmapResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyData = weeklyResult.getOrNull(),
                        heatmapData = heatmapResult.getOrNull()
                    )
                }
            } else {
                val errorMsg = weeklyResult.exceptionOrNull()?.let { ErrorParser.parse(it) }
                    ?: heatmapResult.exceptionOrNull()?.let { ErrorParser.parse(it) }
                    ?: "Failed to load analytics data"
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
            }
        }
    }
}