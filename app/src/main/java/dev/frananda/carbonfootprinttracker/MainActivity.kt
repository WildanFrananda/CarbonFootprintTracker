package dev.frananda.carbonfootprinttracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dagger.hilt.android.AndroidEntryPoint
import dev.frananda.carbonfootprinttracker.core.utils.AuthEvent
import dev.frananda.carbonfootprinttracker.core.utils.AuthEventBus
import dev.frananda.carbonfootprinttracker.ui.features.auth.AuthViewModel
import dev.frananda.carbonfootprinttracker.ui.features.auth.ForgotPasswordScreen
import dev.frananda.carbonfootprinttracker.ui.features.auth.LoginScreen
import dev.frananda.carbonfootprinttracker.ui.features.auth.RegisterScreen
import dev.frananda.carbonfootprinttracker.ui.features.auth.ResetPasswordScreen
import dev.frananda.carbonfootprinttracker.ui.features.main.MainScreen
import dev.frananda.carbonfootprinttracker.ui.navigation.Screen
import dev.frananda.carbonfootprinttracker.ui.theme.CarbonFootprintTrackerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authEventBus: AuthEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarbonFootprintTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CarbonApp(authEventBus = authEventBus)
                }
            }
        }
    }
}

@Composable
fun CarbonApp(
    authViewModel: AuthViewModel = hiltViewModel(),
    authEventBus: AuthEventBus
): Unit {
    val navController = rememberNavController()
    val context = LocalContext.current
    val startDestination = remember {
        if (authViewModel.checkLoginStatus()) {
            Screen.Main.route
        } else {
            Screen.Login.route
        }
    }

    LaunchedEffect(Unit) {
        authEventBus.events.collect { event ->
            when (event) {
                AuthEvent.RATE_LIMITED -> {
                    Toast.makeText(context, "Too much request try again later.", Toast.LENGTH_SHORT).show()
                }
                AuthEvent.UNAUTHORIZED_LOGOUT -> {
                    Toast.makeText(context, "Unauthorized logout", Toast.LENGTH_LONG).show()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ResetPassword.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "carbonfootprinttracker://reset-password?token={token}"
                }
            ),
            arguments = listOf(
                navArgument("token") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            ResetPasswordScreen(
                token = token,
                onResetSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
