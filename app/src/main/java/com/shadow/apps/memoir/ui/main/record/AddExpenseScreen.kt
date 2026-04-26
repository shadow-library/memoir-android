package com.shadow.apps.memoir.ui.main.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shadow.apps.memoir.ui.theme.Cyan6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onClose: () -> Unit,
    viewModel: AddExpenseViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isSaveEnabled = uiState.amount.toDoubleOrNull()?.let { it > 0 } == true &&
        uiState.selectedCategoryId != null && !uiState.isSaving

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onClose() }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "New expense",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.submit() },
                        enabled = isSaveEnabled,
                    ) {
                        Text(
                            text = if (uiState.isSaving) "Saving…" else "Save",
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSaveEnabled) Cyan6
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
            ExpenseForm(
                amount = uiState.amount,
                currency = uiState.currency,
                merchant = uiState.merchant,
                selectedCategoryId = uiState.selectedCategoryId,
                note = uiState.note,
                date = uiState.date,
                currencies = uiState.currencies,
                categories = uiState.categories,
                isLoadingCurrencies = uiState.isLoadingCurrencies,
                onAmountChange = viewModel::onAmountChange,
                onCurrencyChange = viewModel::onCurrencyChange,
                onMerchantChange = viewModel::onMerchantChange,
                onCategoryChange = viewModel::onCategoryChange,
                onNoteChange = viewModel::onNoteChange,
                onDateChange = viewModel::onDateChange,
            )
        }
    }
}
