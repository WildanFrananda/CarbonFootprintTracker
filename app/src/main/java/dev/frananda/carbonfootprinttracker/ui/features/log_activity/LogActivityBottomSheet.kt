package dev.frananda.carbonfootprinttracker.ui.features.log_activity

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.frananda.carbonfootprinttracker.core.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogActivityBottomSheet(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: LogActivityViewModel = hiltViewModel()
): Unit {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var expandedSubcategory by remember { mutableStateOf(false) }
    var selectedSubcategory by remember { mutableStateOf("") }

    var quantity by remember { mutableStateOf("") }

    val uniqueCategories = remember(uiState.factors) {
        uiState.factors.map { it.category }.distinct()
    }

    val filteredSubcategories = remember(selectedCategory, uiState.factors) {
        uiState.factors.filter { it.category == selectedCategory }.map { it.subcategory }
    }

    val dynamicUnit = remember(
        selectedCategory,
        selectedSubcategory,
        uiState.factors
    ) {
        uiState.factors.find {
            it.category == selectedCategory && it.subcategory == selectedSubcategory
        }?.unit ?: "unit"
    }

    LaunchedEffect(uniqueCategories) {
        if (uniqueCategories.isNotEmpty() && selectedCategory.isNotEmpty()) {
            selectedCategory = uniqueCategories.first()
            selectedSubcategory = filteredSubcategories.firstOrNull() ?: ""
        }
    }

    LaunchedEffect(uiState.submitState) {
        when (val state = uiState.submitState) {
            is Resource.Success -> {
                viewModel.resetSubmitState()
                Toast.makeText(context, "Activity logged successfully", Toast.LENGTH_SHORT).show()
                onSuccess()
                onDismiss()
            }
            is Resource.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    LaunchedEffect(uiState.fetchError) {
        uiState.fetchError?.let {
            Toast.makeText(context, "Failed to fetch emission factors: $it", Toast.LENGTH_LONG).show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            Text(
                text = "Records new emissions",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (uiState.isLoadingFactors) {
                LinearProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                )
            }

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { if (!uiState.isLoadingFactors) expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = selectedCategory.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
                    },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = !uiState.isLoadingFactors)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    uniqueCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                selectedCategory = category
                                selectedSubcategory = uiState.factors.firstOrNull { it.category == category }?.subcategory ?: ""
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { if (filteredSubcategories.isNotEmpty()) expandedSubcategory = !expandedSubcategory }
            ) {
                OutlinedTextField(
                    value = selectedSubcategory.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("specific Activity") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubcategory) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = filteredSubcategories.isNotEmpty())
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedSubcategory,
                    onDismissRequest = { expandedSubcategory = false }
                ) {
                    filteredSubcategories.forEach { subCat ->
                        DropdownMenuItem(
                            text = { Text(subCat.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                selectedSubcategory = subCat
                                expandedSubcategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity ($dynamicUnit)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.submitActivity(selectedCategory, selectedSubcategory, quantity) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                enabled = uiState.submitState !is Resource.Loading &&
                        selectedCategory.isNotBlank() &&
                        selectedSubcategory.isNotBlank() &&
                        quantity.isNotBlank()
            ) {
                if (uiState.submitState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Submit")
                }
            }
        }
    }
}