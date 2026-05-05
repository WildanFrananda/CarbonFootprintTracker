package dev.frananda.carbonfootprinttracker.ui.features.auth

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.frananda.carbonfootprinttracker.core.utils.Resource
import dev.frananda.carbonfootprinttracker.data.remote.LoginRequest
import dev.frananda.carbonfootprinttracker.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success should emit Loading then Success`() = runTest {
        val request = LoginRequest("test@example.com", "password123")
        coEvery { authRepository.login(request) } returns Result.success(Unit)

        viewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(Resource.Idle)
            
            viewModel.login(request)
            
            assertThat(awaitItem()).isEqualTo(Resource.Loading)
            assertThat(awaitItem()).isEqualTo(Resource.Success(Unit))
        }
    }

    @Test
    fun `login failure should emit Loading then Error`() = runTest {
        val request = LoginRequest("test@example.com", "wrong")
        val errorMessage = "Invalid credentials"
        coEvery { authRepository.login(request) } returns Result.failure(Exception(errorMessage))

        viewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(Resource.Idle)
            
            viewModel.login(request)
            
            assertThat(awaitItem()).isEqualTo(Resource.Loading)
            val errorItem = awaitItem()
            assertThat(errorItem).isInstanceOf(Resource.Error::class.java)
            // ErrorParser might transform the exception, but we check if it's an Error state
        }
    }

    @Test
    fun `checkLoginStatus should return true when repository says so`() {
        every { authRepository.isLoggedIn() } returns true
        
        val result = viewModel.checkLoginStatus()
        
        assertThat(result).isTrue()
        verify { authRepository.isLoggedIn() }
    }

    @Test
    fun `logout should call repository logout`() {
        viewModel.logout()
        
        verify { authRepository.logout() }
    }

    @Test
    fun `resetState should set authState to Idle`() = runTest {
        // First set it to something else
        val request = LoginRequest("test@example.com", "pass")
        coEvery { authRepository.login(request) } returns Result.success(Unit)
        
        viewModel.login(request)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertThat(viewModel.authState.value).isEqualTo(Resource.Success(Unit))
        
        viewModel.resetState()
        
        assertThat(viewModel.authState.value).isEqualTo(Resource.Idle)
    }
}
