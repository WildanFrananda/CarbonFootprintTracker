package dev.frananda.carbonfootprinttracker.ui.features.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.frananda.carbonfootprinttracker.ui.components.DonutChart
import dev.frananda.carbonfootprinttracker.ui.features.auth.AuthViewModel
import dev.frananda.carbonfootprinttracker.ui.features.log_activity.LogActivityBottomSheet

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
): Unit {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val isGreenDay = uiState.dailyData?.is_green_day ?: false
    val targetBackgroundColor = if (isGreenDay) {
        MaterialTheme.colorScheme.background
    } else {
        Color(0xFFFFF0F0)
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetBackgroundColor,
        animationSpec = tween(durationMillis = 800),
        label = "BgColorAnim"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = animatedBackgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor =  if (isGreenDay) MaterialTheme.colorScheme.primary else Color(0xFFD32F2F)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Record Emission", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "Error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            text = if (isGreenDay) "Great! Your emission is green 🌱" else "Warning! Your Emission is not green ⚠️",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (isGreenDay) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    uiState.dailyData?.let { data ->
                        item {
                            if (data.total_emission > 0) {
                                DonutChart(
                                    emissions = data.breakdown,
                                    totalEmission = data.total_emission
                                )
                            } else {
                                Text("No data available")
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }

                    if (uiState.recommendations.isNotEmpty()) {
                        item {
                            Text(
                                text = "AI Recommendation",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.Start as Alignment)
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )
                        }

                        items(uiState.recommendations) { tip ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = tip,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                        Button(
                            onClick = {
                                authViewModel.logout()
                                onNavigateToLogin()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Logout")
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            LogActivityBottomSheet(
                onDismiss = { showBottomSheet = false },
                onSuccess = {
                    homeViewModel.loadDashboardData()
                }
            )
        }
    }
}