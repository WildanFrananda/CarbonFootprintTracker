package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.data.remote.ForgotPasswordRequest
import dev.frananda.carbonfootprinttracker.data.remote.GoogleLoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.LoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.RegisterRequest
import dev.frananda.carbonfootprinttracker.data.remote.ResetPasswordRequest

class FakeAuthRepository : AuthRepository {
    var shouldSuccess: Boolean = true
    private var isUserLoggedIn: Boolean = false

    override suspend fun login(request: LoginRequest): Result<Unit> {
        return if (shouldSuccess) {
            isUserLoggedIn = true
            Result.success(Unit)
        } else {
            Result.failure(Exception("Login Failed"))
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> = Result.success(Unit)

    override suspend fun googleLogin(request: GoogleLoginRequest): Result<Unit> = Result.success(Unit)

    override suspend fun forgotPassword(request: ForgotPasswordRequest): Result<Unit> = Result.success(Unit)

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit> = Result.success(Unit)

    override fun isLoggedIn(): Boolean = isUserLoggedIn

    override fun logout() { isUserLoggedIn = false }
}