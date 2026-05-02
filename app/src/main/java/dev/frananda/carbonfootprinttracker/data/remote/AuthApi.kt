package dev.frananda.carbonfootprinttracker.data.remote

import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
data class BaseResponse<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null
)

@Serializable
data class AuthTokens(
    val access_token: String,
    val refresh_token: String,
    val user_id: String,
    val display_name: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val display_name: String
)

@Serializable
data class RefreshRequest(
    val refresh_token: String
)

@Serializable
data class GoogleLoginRequest(val id_token: String)

@Serializable
data class ForgotPasswordRequest(val email: String)

@Serializable
data class ResetPasswordRequest(val token: String, val new_password: String)

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<AuthTokens>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<AuthTokens>

    @POST("/api/auth/refresh")
    fun refreshTokenSync(@Body request: RefreshRequest): Call<BaseResponse<AuthTokens>>

    @POST("/api/auth/google")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): BaseResponse<AuthTokens>

    @POST("/api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): BaseResponse<Unit?>

    @POST("/api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): BaseResponse<Unit?>
}