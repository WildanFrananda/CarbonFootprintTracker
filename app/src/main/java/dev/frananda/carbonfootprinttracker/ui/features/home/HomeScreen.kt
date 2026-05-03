package dev.frananda.carbonfootprinttracker.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.frananda.carbonfootprinttracker.R
import dev.frananda.carbonfootprinttracker.ui.features.home.components.CurrentMonthCard
import dev.frananda.carbonfootprinttracker.ui.features.home.components.RecommendationCard
import dev.frananda.carbonfootprinttracker.ui.features.home.components.WeeklyActivityCard
import dev.frananda.carbonfootprinttracker.ui.features.log_activity.LogActivityBottomSheet
import dev.frananda.carbonfootprinttracker.ui.theme.Spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
): Unit {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.carbon_tracker),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "CarbonTracker",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notification */ }) {
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true},
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = Spacing.sm)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Record Emission")
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
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Spacing.md),
                ) {
                    item {
                        Column(modifier = Modifier.padding(vertical = Spacing.sm)) {
                            Text(
                                text = "Hello, Wildan Frananda!",
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 32.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Here is your eco-impact summary.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF6B7280)
                            )
                        }
                        Spacer(modifier = Modifier.height(Spacing.md))
                    }

                    item {
                        CurrentMonthCard(emission = uiState.dailyData?.total_emission?.toString() ?: "0.0")
                        Spacer(modifier = Modifier.height(Spacing.md))
                    }

                    item {
                        WeeklyActivityCard()
                        Spacer(modifier = Modifier.height(Spacing.md))
                    }

                    item {
                        RecommendationCard(
                            recommendation = uiState.recommendations ?: "Swapping one beef meal for a plant-based option today can reduce your food footprint by 60%."
                        )
                        Spacer(modifier = Modifier.height(Spacing.xl))
                    }
                }
            }
        }

        if (showBottomSheet) {
            LogActivityBottomSheet(
                onDismiss = { showBottomSheet = false },
                onSuccess = {
                    showBottomSheet = false
                    homeViewModel.loadDashboardData()
                    scope.launch {
                        snackbarHostState.showSnackbar("Activity logged! Check your achievements for new badges.")
                    }
                }
            )
        }
    }
}