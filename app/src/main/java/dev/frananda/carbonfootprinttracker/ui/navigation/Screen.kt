package dev.frananda.carbonfootprinttracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

/**
 * Root Graphs
 */
@Serializable object AuthGraph
@Serializable
object MainGraph

/**
 * Auth Destinations
 */
@Serializable object Login
@Serializable object Register
@Serializable object ForgotPassword
@Serializable data class ResetPassword(val token: String)

/**
 * Main Destinations
 */
@Serializable object Home
@Serializable object History
@Serializable object Analytics
@Serializable object Profile

sealed class BottomNavItem(val route: Any, val title: String, val icon: ImageVector) {
    object HomeItem : BottomNavItem(Home, "Home", Icons.Filled.Home)
    object HistoryItem : BottomNavItem(History, "History", Icons.Filled.History)
    object AnalyticsItem : BottomNavItem(Analytics, "Analytics", Icons.Filled.Info)
    object ProfileItem : BottomNavItem(Profile, "Profile", Icons.Filled.Person)

    companion object {
        val items = listOf(HomeItem, HistoryItem, AnalyticsItem, ProfileItem)
    }
}