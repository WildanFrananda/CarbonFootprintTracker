package dev.frananda.carbonfootprinttracker.ui.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.core.utils.DateUtils
import dev.frananda.carbonfootprinttracker.data.remote.ActivityApi
import dev.frananda.carbonfootprinttracker.data.remote.ActivityResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryuUiState(
    val isLoading: Boolean = true,
    val activities: List<ActivityResponseDto> = emptyList(),
    val currentDate: String = "",
    val error: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val activityApi: ActivityApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryuUiState())
    val uiState: StateFlow<HistoryuUiState> = _uiState.asStateFlow()

    init {
        loadTodayActivities()
    }

    fun loadTodayActivities(): Unit {
        val today = DateUtils.getCurrentDateInLocalTimezone()
        _uiState.update { it.copy(currentDate = today) }
        fetchActivitiesByDate(today)
    }

    private fun fetchActivitiesByDate(date: String): Unit {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = activityApi.getActivities(date)
                if (response.status == "success" && response.data != null) {
                    _uiState.update { it.copy(isLoading = false, activities = response.data) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = response.message ?: "Failed to fetch activities") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = ErrorParser.parse(e)) }
            }
        }
    }

    fun deleteActivity(id: Int): Unit {
        viewModelScope.launch {
            try {
                val response = activityApi.deleteActivity(id)
                if (response.status == "success") {
                    fetchActivitiesByDate(_uiState.value.currentDate)
                } else {
                    _uiState.update { it.copy(error = response.message ?: "Failed to delete activity") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = ErrorParser.parse(e)) }
            }
        }
    }
}