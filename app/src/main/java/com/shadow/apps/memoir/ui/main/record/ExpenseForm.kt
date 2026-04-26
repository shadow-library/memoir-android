package com.shadow.apps.memoir.ui.main.record

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadow.apps.memoir.domain.model.Category
import com.shadow.apps.memoir.domain.model.EnabledCurrency
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Cyan6
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate4
import com.shadow.apps.memoir.ui.theme.Slate6
import com.shadow.apps.memoir.ui.theme.Slate7
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Currency

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

private fun currencySymbol(code: String): String = runCatching {
    Currency.getInstance(code).symbol
}.getOrDefault(code)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseForm(
    amount: String,
    currency: String,
    merchant: String,
    selectedCategoryId: String?,
    note: String,
    date: LocalDate,
    currencies: List<EnabledCurrency>,
    categories: List<Category>,
    isLoadingCurrencies: Boolean,
    onAmountChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onMerchantChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val amountFocus = remember { FocusRequester() }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── Amount display ────────────────────────────────────────────────────
        Spacer(Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = currencySymbol(currency),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                ),
                modifier = Modifier.padding(end = 4.dp),
            )
            BasicTextField(
                value = amount,
                onValueChange = { value ->
                    if (value.isEmpty() || value.matches(Regex("^\\d{0,10}(\\.\\d{0,2})?$"))) {
                        onAmountChange(value)
                    }
                },
                modifier = Modifier.focusRequester(amountFocus),
                textStyle = TextStyle(
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(Cyan5),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                decorationBox = { inner ->
                    Box {
                        if (amount.isEmpty()) {
                            Text(
                                text = "0",
                                style = TextStyle(
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                ),
                            )
                        }
                        inner()
                    }
                },
            )
        }

        // ── Currency select ───────────────────────────────────────────────────
        Spacer(Modifier.height(16.dp))
        CurrencySelect(
            currencies = currencies,
            selectedCode = currency,
            onCurrencySelected = onCurrencyChange,
            isLoading = isLoadingCurrencies,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(Modifier.height(20.dp))

        // ── Scan receipt placeholder ──────────────────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .alpha(0.55f),
            shape = RoundedCornerShape(14.dp),
            color = if (isDark) Slate7 else Color.White,
            border = BorderStroke(
                width = 1.dp,
                color = if (isDark) Slate6 else Slate1,
            ),
            onClick = {},
            enabled = false,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Cyan6,
                    modifier = Modifier.size(40.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Scan a receipt",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Coming soon",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Form fields ───────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FormLabel("DATE")
            Surface(
                onClick = { showDatePicker = true },
                shape = RoundedCornerShape(14.dp),
                color = if (isDark) Slate7 else Color.White,
                border = BorderStroke(1.dp, if (isDark) Slate6 else Slate1),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = date.format(dateFormatter),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            FormLabel("MERCHANT")
            OutlinedTextField(
                value = merchant,
                onValueChange = onMerchantChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = fieldColors(isDark),
            )

            FormLabel("CATEGORY")
            CategorySelect(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = onCategoryChange,
            )

            FormLabel("NOTE")
            OutlinedTextField(
                value = note,
                onValueChange = onNoteChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                minLines = 2,
                maxLines = 4,
                colors = fieldColors(isDark),
            )

            // Link to subscription (placeholder)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isDark) Slate7 else Color.White,
                border = BorderStroke(1.dp, if (isDark) Slate6 else Slate1),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(shape = CircleShape, color = Cyan5.copy(alpha = 0.15f)) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = null,
                            tint = Cyan6,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(18.dp),
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Link to subscription",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "Amortizes across covered months.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                        )
                    }
                    Switch(
                        checked = false,
                        onCheckedChange = null,
                        enabled = false,
                        colors = SwitchDefaults.colors(
                            disabledUncheckedThumbColor = Color.White,
                            disabledUncheckedTrackColor = if (isDark) Slate6 else Slate1,
                            disabledUncheckedBorderColor = if (isDark) Slate6 else Slate1,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selected = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDateChange(selected)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
internal fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
        letterSpacing = 0.8.sp,
    )
}

@Composable
internal fun fieldColors(isDark: Boolean) = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = if (isDark) Slate7 else Color.White,
    focusedContainerColor = if (isDark) Slate7 else Color.White,
    focusedBorderColor = Cyan5,
    unfocusedBorderColor = if (isDark) Slate6 else Slate1,
    cursorColor = Cyan5,
    unfocusedLabelColor = Slate4,
    focusedLabelColor = Cyan6,
)
