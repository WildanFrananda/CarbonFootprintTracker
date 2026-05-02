package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.ForgotPasswordRequest
import dev.frananda.carbonfootprinttracker.data.remote.GoogleLoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.LoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.RegisterRequest
import dev.frananda.carbonfootprinttracker.data.remote.ResetPasswordRequest

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<Unit>
    suspend fun register(request: RegisterRequest): Result<Unit>

    suspend fun googleLogin(request: GoogleLoginRequest): Result<Unit>
    suspend fun forgotPassword(request: ForgotPasswordRequest): Result<Unit>
    suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit>
    fun isLoggedIn(): Boolean
    fun logout()
}