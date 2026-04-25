package com.shadow.apps.memoir.ui.onboarding.devicesetup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shadow.apps.memoir.R
import com.shadow.apps.memoir.domain.model.CurrencyOption
import com.shadow.apps.memoir.ui.onboarding.components.PageDots
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Cyan6
import com.shadow.apps.memoir.ui.theme.ShadowMemoirTheme
import com.shadow.apps.memoir.ui.theme.Slate0
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate6
import com.shadow.apps.memoir.ui.theme.Slate7
import com.shadow.apps.memoir.ui.theme.Slate8
import com.shadow.apps.memoir.ui.theme.Slate9

private val PreviewCurrencies = listOf(
    CurrencyOption("INR", "Indian Rupee"),
    CurrencyOption("AED", "United Arab Emirates Dirham"),
)

@Composable
fun DeviceSetupScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    viewModel: DeviceSetupViewModel = hiltViewModel(),
) {
    BackHandler(onBack = onBack)
    val uiState by viewModel.uiState.collectAsState()

    DeviceSetupContent(
        uiState = uiState,
        onBack = onBack,
        onDeviceNameChange = viewModel::onDeviceNameChange,
        onIsPrimaryChange = viewModel::onIsPrimaryChange,
        onCurrencySelect = viewModel::onCurrencySelect,
        onFinish = { viewModel.finishSetup(onContinue) },
    )
}

@Composable
private fun DeviceSetupContent(
    uiState: DeviceSetupUiState,
    onBack: () -> Unit,
    onDeviceNameChange: (String) -> Unit,
    onIsPrimaryChange: (Boolean) -> Unit,
    onCurrencySelect: (String) -> Unit,
    onFinish: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val backgroundModifier = if (isDark) {
        Modifier.background(Brush.verticalGradient(listOf(Slate9, Slate8)))
    } else {
        Modifier.background(Slate0)
    }

    if (uiState.isLoadingProfile) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(backgroundModifier),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = Cyan5, strokeWidth = 2.dp)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(backgroundModifier),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(48.dp))

            Surface(
                onClick = onBack,
                color = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.back),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f),
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.device_setup_step),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.5.sp,
                color = Cyan5,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.device_setup_headline),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.device_setup_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.device_setup_device_name_label),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f),
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.deviceName,
                onValueChange = onDeviceNameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = if (isDark) Slate7 else Color.White,
                    focusedContainerColor = if (isDark) Slate7 else Color.White,
                    unfocusedBorderColor = if (isDark) Slate6 else Slate1,
                    focusedBorderColor = Cyan5,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = Cyan5,
                ),
            )

            Spacer(Modifier.height(12.dp))

            PrimaryDeviceCard(
                isPrimary = uiState.isPrimary,
                onToggle = onIsPrimaryChange,
                isDark = isDark,
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.device_setup_currency_label),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.50f),
            )

            Spacer(Modifier.height(12.dp))

            CurrencySelectBox(
                currencies = uiState.currencies,
                selectedCurrency = uiState.selectedCurrency,
                isLoading = uiState.isLoadingCurrencies,
                isDark = isDark,
                onCurrencySelect = onCurrencySelect,
            )

            if (uiState.isLoadingCurrencies || uiState.currencyError != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = uiState.currencyError ?: "Loading currency options…",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (uiState.currencyError == null) {
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
            }

            PageDots(total = 5, current = 4)

            Spacer(Modifier.height(16.dp))

            FinishButton(isLoading = uiState.isSaving, onClick = onFinish)

            Spacer(Modifier.height(36.dp))
        }
    }
}

@Composable
private fun PrimaryDeviceCard(
    isPrimary: Boolean,
    onToggle: (Boolean) -> Unit,
    isDark: Boolean,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = if (isDark) Slate7 else Color.White,
        border = BorderStroke(width = 1.dp, color = if (isDark) Slate6 else Slate1),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.device_setup_primary_title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.device_setup_primary_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                )
            }
            Spacer(Modifier.width(16.dp))
            Switch(
                checked = isPrimary,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Cyan5,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = if (isDark) Slate6 else Slate1,
                    uncheckedBorderColor = if (isDark) Slate6 else Slate1,
                ),
            )
        }
    }
}

@Composable
private fun CurrencySelectBox(
    currencies: List<CurrencyOption>,
    selectedCurrency: String,
    isLoading: Boolean,
    isDark: Boolean,
    onCurrencySelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCurrencyLabel = currencies.firstOrNull { it.code == selectedCurrency }?.label ?: selectedCurrency

    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            onClick = { if (!isLoading && currencies.isNotEmpty()) expanded = true },
            enabled = !isLoading && currencies.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = if (isDark) Slate7 else Color.White,
            border = BorderStroke(width = 1.dp, color = if (isDark) Slate6 else Slate1),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selectedCurrencyLabel,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.88f),
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = currency.label,
                            fontWeight = if (currency.code == selectedCurrency) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            },
                            color = if (currency.code == selectedCurrency) {
                                Cyan6
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    },
                    onClick = {
                        onCurrencySelect(currency.code)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun FinishButton(isLoading: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Cyan5,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White,
                )
            } else {
                Text(
                    text = stringResource(R.string.device_setup_finish),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }
}

/**
 * Previews
 */

@Preview(name = "DeviceSetup – Light", showBackground = true, showSystemUi = true)
@Composable
private fun DeviceSetupLightPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        DeviceSetupContent(
            uiState = DeviceSetupUiState(
                deviceName = "My Pixel 9",
                isPrimary = false,
                selectedCurrency = "INR",
                currencies = PreviewCurrencies,
                isLoadingProfile = false,
            ),
            onBack = {},
            onDeviceNameChange = {},
            onIsPrimaryChange = {},
            onCurrencySelect = {},
            onFinish = {},
        )
    }
}

@Preview(name = "DeviceSetup – Dark", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DeviceSetupDarkPreview() {
    ShadowMemoirTheme(darkTheme = true) {
        DeviceSetupContent(
            uiState = DeviceSetupUiState(
                deviceName = "My Pixel 9",
                isPrimary = true,
                selectedCurrency = "INR",
                currencies = PreviewCurrencies,
                isLoadingProfile = false,
            ),
            onBack = {},
            onDeviceNameChange = {},
            onIsPrimaryChange = {},
            onCurrencySelect = {},
            onFinish = {},
        )
    }
}

@Preview(name = "DeviceSetup – Error", showBackground = true, showSystemUi = true)
@Composable
private fun DeviceSetupErrorPreview() {
    ShadowMemoirTheme(darkTheme = false) {
        DeviceSetupContent(
            uiState = DeviceSetupUiState(
                deviceName = "My Pixel 9",
                isPrimary = true,
                selectedCurrency = "USD",
                currencies = PreviewCurrencies,
                isLoadingProfile = false,
                error = "Setup failed. Please try again.",
            ),
            onBack = {},
            onDeviceNameChange = {},
            onIsPrimaryChange = {},
            onCurrencySelect = {},
            onFinish = {},
        )
    }
}
