package dev.frananda.carbonfootprinttracker.core.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class AuthEvent {
    UNAUTHORIZED_LOGOUT,
    RATE_LIMITED
}

@Singleton
class AuthEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun emitLogoutEvent(): Unit {
        _events.tryEmit(AuthEvent.UNAUTHORIZED_LOGOUT)
    }

    fun emitRateLimitEvent(): Unit {
        _events.tryEmit(AuthEvent.RATE_LIMITED)
    }
}