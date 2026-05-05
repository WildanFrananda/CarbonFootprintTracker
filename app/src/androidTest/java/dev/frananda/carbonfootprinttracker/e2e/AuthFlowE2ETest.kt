package dev.frananda.carbonfootprinttracker.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.frananda.carbonfootprinttracker.MainActivity
import dev.frananda.carbonfootprinttracker.di.RepositoryModule
import dev.frananda.carbonfootprinttracker.domain.repository.ActivityRepository
import dev.frananda.carbonfootprinttracker.domain.repository.AuthRepository
import dev.frananda.carbonfootprinttracker.domain.repository.DashboardRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeActivityRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeAuthRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeDashboardRepository
import dev.frananda.carbonfootprinttracker.domain.repository.FakeUserRepository
import dev.frananda.carbonfootprinttracker.domain.repository.UserRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class AuthFlowE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @BindValue @JvmField val authRepository: AuthRepository = FakeAuthRepository()
    @BindValue @JvmField val activityRepository: ActivityRepository = FakeActivityRepository()
    @BindValue @JvmField val dashboardRepository: DashboardRepository = FakeDashboardRepository()
    @BindValue @JvmField val userRepository: UserRepository = FakeUserRepository()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun loginFlow_success() {
        (authRepository as FakeAuthRepository).logout()
        
        composeTestRule.onNodeWithText("nature@carbon.com").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("••••••••").performTextInput("password123")
        
        composeTestRule.onNodeWithText("Login").performClick()
        
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasText("Hello, Wildan Frananda!")).fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Hello, Wildan Frananda!").assertIsDisplayed()
    }
}
