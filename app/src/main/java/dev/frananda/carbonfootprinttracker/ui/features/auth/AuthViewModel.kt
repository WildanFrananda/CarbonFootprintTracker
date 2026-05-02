package dev.frananda.carbonfootprinttracker.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.core.utils.Resource
import dev.frananda.carbonfootprinttracker.data.remote.ForgotPasswordRequest
import dev.frananda.carbonfootprinttracker.data.remote.GoogleLoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.LoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.RegisterRequest
import dev.frananda.carbonfootprinttracker.data.remote.ResetPasswordRequest
import dev.frananda.carbonfootprinttracker.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val authState: StateFlow<Resource<Unit>> = _authState.asStateFlow()

    fun checkLoginStatus(): Boolean {
        return authRepository.isLoggedIn()
    }

    fun login(request: LoginRequest): Unit {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.login(request)
            result.onSuccess {
                _authState.value = Resource.Success(Unit)
            }.onFailure { exception ->
                val errorMessage = ErrorParser.parse(exception)
                _authState.value = Resource.Error(errorMessage)
            }
        }
    }

    fun register(request: RegisterRequest): Unit {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.register(request)
            result.onSuccess {
                _authState.value = Resource.Success(Unit)
            }.onFailure { exception ->
                val errorMessage = ErrorParser.parse(exception)
                _authState.value = Resource.Error(errorMessage)
            }
        }
    }

    fun loginWithGoogle(request: GoogleLoginRequest): Unit {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.googleLogin(request)
            result.onSuccess {
                _authState.value = Resource.Success(Unit)
            }.onFailure { exception ->
                val errorMessage = ErrorParser.parse(exception)
                _authState.value = Resource.Error(errorMessage)
            }
        }
    }

    fun requestForgotPassword(request: ForgotPasswordRequest): Unit {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.forgotPassword(request)
            result.onSuccess {
                _authState.value = Resource.Success(Unit)
            }.onFailure { exception ->
                val errorMessage = ErrorParser.parse(exception)
                _authState.value = Resource.Error(errorMessage)
            }
        }
    }

    fun submitResetPassword(request: ResetPasswordRequest): Unit {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.resetPassword(request)
            result.onSuccess {
                _authState.value = Resource.Success(Unit)
            }.onFailure { exception ->
                val errorMessage = ErrorParser.parse(exception)
                _authState.value = Resource.Error(errorMessage)
            }
        }
    }

    fun logout(): Unit {
        authRepository.logout()
    }

    fun resetState(): Unit {
        _authState.value = Resource.Idle
    }
}
