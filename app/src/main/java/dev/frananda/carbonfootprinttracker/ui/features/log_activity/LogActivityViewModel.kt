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
import javax.inject.Inject

class LogActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private val _submitState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val submitState: StateFlow<Resource<Unit>> = _submitState.asStateFlow()

    fun submitActivity(category: String, subcategory: String, quantityStr: String): Unit {
        val quantity = quantityStr.toDoubleOrNull()
        if (quantity == null || quantity <= 0) {
            _submitState.value = Resource.Error("Invalid quantity and above 0")
            return
        }

        viewModelScope.launch {
            _submitState.value = Resource.Loading
            val request = ActivityRequest(
                category = category,
                subcategory = subcategory,
                quantity = quantity,
                date = DateUtils.getCurrentDateInLocalTimezone()
            )

            val result = activityRepository.logActivity(request)

            result.onSuccess {
                _submitState.value = Resource.Success(Unit)
            }.onFailure { exception ->
                _submitState.value = Resource.Error(ErrorParser.parse(exception))
            }
        }
    }

    fun resetState(): Unit {
        _submitState.value = Resource.Idle
    }
}