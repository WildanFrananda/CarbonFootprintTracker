package dev.frananda.carbonfootprinttracker.ui.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.frananda.carbonfootprinttracker.ui.features.analytics.AnalyticsScreen
import dev.frananda.carbonfootprinttracker.ui.features.history.HistoryScreen
import dev.frananda.carbonfootprinttracker.ui.features.home.HomeScreen
import dev.frananda.carbonfootprinttracker.ui.features.profile.ProfileScreen
import dev.frananda.carbonfootprinttracker.ui.navigation.Analytics
import dev.frananda.carbonfootprinttracker.ui.navigation.BottomNavItem
import dev.frananda.carbonfootprinttracker.ui.navigation.History
import dev.frananda.carbonfootprinttracker.ui.navigation.Home
import dev.frananda.carbonfootprinttracker.ui.navigation.Profile

@Composable
fun MainScreen(): Unit {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                BottomNavItem.items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home> { HomeScreen() }
            composable<History> { HistoryScreen() }
            composable<Analytics> { AnalyticsScreen() }
            composable<Profile> { ProfileScreen() }
        }
    }
}