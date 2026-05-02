package dev.frananda.carbonfootprinttracker.ui.features.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.frananda.carbonfootprinttracker.core.utils.Resource
import dev.frananda.carbonfootprinttracker.data.remote.ResetPasswordRequest

@Composable
fun ResetPasswordScreen(
    token: String,
    onResetSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
): Unit {
    var newPassword by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is Resource.Success -> {
                onResetSuccess()
                viewModel.resetState()
            }
            is Resource.Error -> {
                val message = (authState as Resource.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Reset Password", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.submitResetPassword(ResetPasswordRequest(token, newPassword))
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = authState !is Resource.Loading
        ) {
            if (authState is Resource.Loading) CircularProgressIndicator()
            else Text("Save New Password")
        }
    }
}