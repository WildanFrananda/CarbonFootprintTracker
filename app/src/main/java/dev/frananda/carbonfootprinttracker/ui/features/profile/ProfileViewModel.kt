package dev.frananda.carbonfootprinttracker.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.data.remote.BadgeDto
import dev.frananda.carbonfootprinttracker.data.remote.GamificationApi
import dev.frananda.carbonfootprinttracker.data.remote.UserProfileDto
import dev.frananda.carbonfootprinttracker.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfileDto? = null,
    val badges: List<BadgeDto> = emptyList(),
    val error: String? = null,
    val isUpdatingTarget: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val gamificationApi: GamificationApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData(): Unit {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val profileDeferred = async { userRepository.getUserProfile() }
            val badgesDeferred = async {
                try {
                    gamificationApi.getBadges().data ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }

            val profileResult = profileDeferred.await()
            val badgesResult = badgesDeferred.await()

            profileResult.onSuccess { profileData ->
                _uiState.update {
                    it.copy(isLoading = false, profile = profileData, badges = badgesResult)
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, error = ErrorParser.parse(exception))
                }
            }
        }
    }

    fun updateDailyTarget(newTarget: String): Unit {
        val targetValue = newTarget.toDoubleOrNull()

        if (targetValue == null || targetValue <= 0) {
            _uiState.update { it.copy(error = "Invalid target value") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingTarget = true, error = null) }
            val result = userRepository.updateTarget(targetValue)

            result.onSuccess { updatedProfile ->
                _uiState.update { it.copy(isUpdatingTarget = false, profile = updatedProfile) }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isUpdatingTarget = false, error = ErrorParser.parse(exception))
                }
            }
        }
    }

    fun clearError(): Unit {
        _uiState.update { it.copy(error = null) }
    }
}