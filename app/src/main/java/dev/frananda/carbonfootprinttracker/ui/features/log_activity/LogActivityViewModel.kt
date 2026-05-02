package dev.frananda.carbonfootprinttracker.ui.features.log_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.core.utils.DateUtils
import dev.frananda.carbonfootprinttracker.core.utils.Resource
import dev.frananda.carbonfootprinttracker.data.remote.ActivityRequest
import dev.frananda.carbonfootprinttracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.frananda.carbonfootprinttracker.data.remote.EmissionFactorDto
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LogActivitiesUiState(
    val factors: List<EmissionFactorDto> = emptyList(),
    val isLoadingFactors: Boolean = true,
    val submitState: Resource<Unit> = Resource.Idle,
    val fetchError: String? = null
)

@HiltViewModel
class LogActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LogActivitiesUiState())
    val uiState: StateFlow<LogActivitiesUiState> = _uiState.asStateFlow()

    init {
        fetchEmissionFactors()
    }

    private fun fetchEmissionFactors(): Unit {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFactors = true, fetchError = null) }
            val result = activityRepository.getEmissionFactors()

            result.onSuccess { factorsData ->
                _uiState.update {
                    it.copy(isLoadingFactors = false, factors = factorsData)
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoadingFactors = false, fetchError = ErrorParser.parse(exception))
                }
            }
        }
    }

    fun submitActivity(category: String, subcategory: String, quantityStr: String): Unit {
        val quantity = quantityStr.toDoubleOrNull()
        if (quantity == null || quantity <= 0) {
            _uiState.update { it.copy(submitState = Resource.Error("Invalid quantity")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(submitState = Resource.Loading) }
            val request = ActivityRequest(
                category = category.lowercase(),
                subcategory = subcategory.lowercase(),
                quantity = quantity,
                date = DateUtils.getCurrentDateInLocalTimezone()
            )

            val result = activityRepository.logActivity(request)

            result.onSuccess {
                _uiState.update { it.copy(submitState = Resource.Success(Unit)) }
            }.onFailure { exception ->
                _uiState.update { it.copy(submitState = Resource.Error(ErrorParser.parse(exception))) }
            }
        }
    }

    fun resetSubmitState(): Unit {
        _uiState.update { it.copy(submitState = Resource.Idle) }
    }
}