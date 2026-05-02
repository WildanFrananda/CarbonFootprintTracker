package dev.frananda.carbonfootprinttracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Main : Screen("main_screen")
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_tab", "Home", Icons.Filled.Home)
    object History : BottomNavItem("history_tab", "History", Icons.Filled.History)
    object Analytics : BottomNavItem("analytics_tab", "Analytics", Icons.Filled.Info)
    object Profile : BottomNavItem("profile_tab", "Profile", Icons.Filled.Person)
}