package dev.frananda.carbonfootprinttracker.core.network

import dev.frananda.carbonfootprinttracker.data.remote.BaseResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

object ErrorParser {
    fun parse(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                val errorBody = throwable.response()?.errorBody()?.string()
                if (errorBody.isNullOrBlank()) return "Server error (${throwable.code()})"
                try {
                    val json = Json { ignoreUnknownKeys = true }
                    val response = json.decodeFromString<BaseResponse<Unit>>(errorBody)
                    response.message ?: "Unknown server error"
                } catch (e: Exception) {
                    "Server error: ${throwable.code()}"
                }
            }

            is IOException -> "No internet connection, check your connection and try again."
            else -> "Unknown error occurred: ${throwable.localizedMessage}"
        }
    }
}