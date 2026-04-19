package dev.frananda.carbonfootprinttracker.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.core.utils.DateUtils
import dev.frananda.carbonfootprinttracker.data.remote.DailyDashboardDto
import dev.frananda.carbonfootprinttracker.domain.repository.DashboardRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val dailyData: DailyDashboardDto? = null,
    val recommendations: List<String> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData(): Unit {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false, error = null) }
            val todayDate = DateUtils.getCurrentDateInLocalTimezone()

            val dailyDeferred = async { dashboardRepository.getDailyDashboard(todayDate) }
            val insightDeferred = async { dashboardRepository.getInsights() }

            val dailyResult = dailyDeferred.await()
            val insightResult = insightDeferred.await()

            if (dailyResult.isSuccess && insightResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        dailyData = dailyResult.getOrNull(),
                        recommendations = insightResult.getOrNull()?.recommendations ?: emptyList()
                    )
                }
            } else {
                val errorMsg = dailyResult.exceptionOrNull()?.let { ErrorParser.parse(it) }
                    ?: insightResult.exceptionOrNull()?.let { ErrorParser.parse(it) }
                    ?: "Unknown error"
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
            }
        }
    }
}