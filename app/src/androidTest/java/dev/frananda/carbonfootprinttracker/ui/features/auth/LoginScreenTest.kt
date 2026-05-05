package dev.frananda.carbonfootprinttracker.ui.features.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.frananda.carbonfootprinttracker.HiltTestActivity
import dev.frananda.carbonfootprinttracker.di.RepositoryModule
import dev.frananda.carbonfootprinttracker.domain.repository.ActivityRepository
import dev.frananda.carbonfootprinttracker.domain.repository.AuthRepository
import dev.frananda.carbonfootprinttracker.domain.repository.DashboardRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeActivityRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeAuthRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeDashboardRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeUserRepository
import dev.frananda.carbonfootprinttracker.domain.repository.UserRepository
import dev.frananda.carbonfootprinttracker.ui.theme.CarbonFootprintTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class LoginScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @BindValue @JvmField val authRepository: AuthRepository = FakeAuthRepository()
    @BindValue @JvmField val activityRepository: ActivityRepository = FakeActivityRepository()
    @BindValue @JvmField val dashboardRepository: DashboardRepository = FakeDashboardRepository()
    @BindValue @JvmField val userRepository: UserRepository = FakeUserRepository()

    @Before
    fun init() {
        hiltRule.inject()
        (authRepository as FakeAuthRepository).logout()
    }

    @Test
    fun loginScreen_elementsAreDisplayed() {
        composeTestRule.setContent {
            CarbonFootprintTrackerTheme {
                LoginScreen(
                    onNavigateToRegister = {},
                    onNavigateToForgotPassword = {},
                    onLoginSuccess = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Welcome Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun loginScreen_inputEmailAndPassword() {
        composeTestRule.setContent {
            CarbonFootprintTrackerTheme {
                LoginScreen(
                    onNavigateToRegister = {},
                    onNavigateToForgotPassword = {},
                    onLoginSuccess = {}
                )
            }
        }

        composeTestRule.onNodeWithText("nature@carbon.com").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("••••••••").performTextInput("password123")
        
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
    }
}
