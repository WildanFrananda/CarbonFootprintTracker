package dev.frananda.carbonfootprinttracker.core.network

import dev.frananda.carbonfootprinttracker.data.remote.BaseResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

object ErrorParser {
    fun parse(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                try {
                    val errorBody = throwable.response()?.errorBody()?.string()
                    if (errorBody != null) {
                        val json = Json { ignoreUnknownKeys = true }
                        val baseResponse = json.decodeFromString<BaseResponse<Any>>(errorBody)
                        baseResponse.message ?: "Server Error"
                    } else {
                        "Unknown error occurred (HTTP ${throwable.code()}"
                    }
                } catch (e: Exception) {
                    "Failed to parse error: ${e.message}"
                }
            }

            is IOException -> "No internet connection, check your connection and try again."
            else -> "Unknown error occurred: ${throwable.message}"
        }
    }
}