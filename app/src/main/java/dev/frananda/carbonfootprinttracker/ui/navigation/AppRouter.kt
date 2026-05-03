package dev.frananda.carbonfootprinttracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import dev.frananda.carbonfootprinttracker.ui.features.auth.ForgotPasswordScreen
import dev.frananda.carbonfootprinttracker.ui.features.auth.LoginScreen
import dev.frananda.carbonfootprinttracker.ui.features.auth.RegisterScreen
import dev.frananda.carbonfootprinttracker.ui.features.auth.ResetPasswordScreen
import dev.frananda.carbonfootprinttracker.ui.features.main.MainScreen

@Composable
fun AppRouter(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation<AuthGraph>(startDestination = Login) {
            composable<Login> {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Register)
                    },
                    onLoginSuccess = {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(ForgotPassword)
                    }
                )
            }

            composable<Register> {
                RegisterScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    }
                )
            }

            composable<ForgotPassword> {
                ForgotPasswordScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<ResetPassword>(
                deepLinks = listOf(
                    navDeepLink<ResetPassword>(basePath = "carbonfootprinttracker://reset-password")
                )
            ) { backStackEntry ->
                val args = backStackEntry.toRoute<ResetPassword>()
                ResetPasswordScreen(
                    token = args.token,
                    onResetSuccess = {
                        navController.navigate(Login) {
                            popUpTo<AuthGraph> { inclusive = true }
                        }
                    }
                )
            }
        }

        composable<MainGraph> {
            MainScreen()
        }
    }
}