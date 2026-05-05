package dev.frananda.carbonfootprinttracker.domain.repository

import com.google.common.truth.Truth.assertThat
import dev.frananda.carbonfootprinttracker.data.local.SecureStorage
import dev.frananda.carbonfootprinttracker.data.remote.AuthApi
import dev.frananda.carbonfootprinttracker.data.remote.LoginRequest
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

class AuthRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var authApi: AuthApi
    private val secureStorage: SecureStorage = mockk(relaxed = true)
    private lateinit var repository: AuthRepositoryImpl

    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val contentType = "application/json".toMediaType()
        authApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(AuthApi::class.java)

        repository = AuthRepositoryImpl(authApi, secureStorage)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login success should save tokens`() = runBlocking {
        val loginResponse = """
            {
                "status": "success",
                "message": "Login successful",
                "data": {
                    "access_token": "access_123",
                    "refresh_token": "refresh_456",
                    "user_id": "user_789",
                    "display_name": "Test User"
                }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(loginResponse).setResponseCode(200))

        val result = repository.login(LoginRequest("test@example.com", "password"))

        assertThat(result.isSuccess).isTrue()
        verify { secureStorage.saveTokens("access_123", "refresh_456") }
    }

    @Test
    fun `login failure should return failure`() = runBlocking {
        val errorResponse = """
            {
                "status": "error",
                "message": "Invalid credentials"
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(errorResponse).setResponseCode(401))

        val result = repository.login(LoginRequest("test@example.com", "wrong"))

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `network error should return failure`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val result = repository.login(LoginRequest("test@example.com", "password"))

        assertThat(result.isFailure).isTrue()
    }
}
