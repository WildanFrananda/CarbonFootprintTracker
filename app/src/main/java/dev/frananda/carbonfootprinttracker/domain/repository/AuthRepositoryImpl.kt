package dev.frananda.carbonfootprinttracker.domain.repository

import dev.frananda.carbonfootprinttracker.core.network.ErrorParser
import dev.frananda.carbonfootprinttracker.data.local.SecureStorage
import dev.frananda.carbonfootprinttracker.data.remote.AuthApi
import dev.frananda.carbonfootprinttracker.data.remote.ForgotPasswordRequest
import dev.frananda.carbonfootprinttracker.data.remote.GoogleLoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.LoginRequest
import dev.frananda.carbonfootprinttracker.data.remote.RegisterRequest
import dev.frananda.carbonfootprinttracker.data.remote.ResetPasswordRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val secureStorage: SecureStorage
) : AuthRepository {
    override suspend fun login(request: LoginRequest): Result<Unit> {
        return try {
            val response = authApi.login(request)
            if (response.status == "success" && response.data != null) {
                secureStorage.saveTokens(
                    accessToken = response.data.access_token,
                    refreshToken = response.data.refresh_token
                )
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Login Failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = authApi.register(request)
            if (response.status == "success" && response.data != null) {
                secureStorage.saveTokens(
                    accessToken = response.data.access_token,
                    refreshToken = response.data.refresh_token
                )
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Registration Failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override suspend fun googleLogin(request: GoogleLoginRequest): Result<Unit> {
        return try {
            val response = authApi.googleLogin(request)
            if (response.status == "success" && response.data != null) {
                secureStorage.saveTokens(
                    accessToken = response.data.access_token,
                    refreshToken = response.data.refresh_token
                )
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Google Sign In Failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest): Result<Unit> {
        return try {
            val response = authApi.forgotPassword(request)
            if (response.status == "success") {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Forgot Password Failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit> {
        return try {
            val response = authApi.resetPassword(request)
            if (response.status == "success") {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Forgot Password Failed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(ErrorParser.parse(e)))
        }
    }

    override fun isLoggedIn(): Boolean {
        return secureStorage.getRefreshToken() != null
    }

    override fun logout(): Unit {
        secureStorage.clearTokens()
    }
}