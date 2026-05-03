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
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.frananda.carbonfootprinttracker.core.utils.AuthEvent
import dev.frananda.carbonfootprinttracker.core.utils.AuthEventBus
import dev.frananda.carbonfootprinttracker.ui.features.auth.AuthViewModel
import dev.frananda.carbonfootprinttracker.ui.navigation.AppRouter
import dev.frananda.carbonfootprinttracker.ui.navigation.AuthGraph
import dev.frananda.carbonfootprinttracker.ui.navigation.MainGraph
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
        if (authViewModel.checkLoginStatus()) MainGraph else AuthGraph
    }

    LaunchedEffect(Unit) {
        authEventBus.events.collect { event ->
            when (event) {
                AuthEvent.RATE_LIMITED -> {
                    Toast.makeText(context, "Too much request try again later.", Toast.LENGTH_SHORT).show()
                }
                AuthEvent.UNAUTHORIZED_LOGOUT -> {
                    Toast.makeText(context, "Unauthorized logout", Toast.LENGTH_LONG).show()
                    navController.navigate(AuthGraph) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    AppRouter(
        navController = navController,
        startDestination = startDestination
    )
}
