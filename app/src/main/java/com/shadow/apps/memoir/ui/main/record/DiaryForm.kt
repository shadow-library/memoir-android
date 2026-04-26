package com.shadow.apps.memoir.ui.main.record

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Cyan6
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate6
import com.shadow.apps.memoir.ui.theme.Slate7
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiaryForm(
    entry: String,
    tags: List<String>,
    tagDraft: String,
    date: LocalDate,
    onEntryChange: (String) -> Unit,
    onTagDraftChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(8.dp))

        // ── Date row ──────────────────────────────────────────────────────────
        FormLabel("DATE")
        Surface(
            onClick = { showDatePicker = true },
            shape = RoundedCornerShape(14.dp),
            color = if (isDark) Slate7 else androidx.compose.ui.graphics.Color.White,
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

        // ── Entry ─────────────────────────────────────────────────────────────
        FormLabel("ENTRY")
        OutlinedTextField(
            value = entry,
            onValueChange = onEntryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(14.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
            placeholder = {
                Text(
                    text = "What's on your mind?",
                    style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                )
            },
            colors = fieldColors(isDark),
        )

        // ── Tags ──────────────────────────────────────────────────────────────
        FormLabel("TAGS")

        if (tags.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                tags.forEach { tag ->
                    InputChip(
                        selected = false,
                        onClick = {},
                        label = { Text(tag) },
                        trailingIcon = {
                            IconButton(
                                onClick = { onRemoveTag(tag) },
                                modifier = Modifier.size(16.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = "Remove $tag",
                                    modifier = Modifier.size(12.dp),
                                )
                            }
                        },
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = Cyan5.copy(alpha = 0.12f),
                            labelColor = Cyan6,
                        ),
                        border = InputChipDefaults.inputChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = Cyan5.copy(alpha = 0.3f),
                        ),
                    )
                }
            }
        }

        OutlinedTextField(
            value = tagDraft,
            onValueChange = onTagDraftChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Add a tag, press Done",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAddTag() }),
            colors = fieldColors(isDark),
            trailingIcon = if (tagDraft.isNotBlank()) {
                {
                    AssistChip(
                        onClick = onAddTag,
                        label = { Text("Add") },
                        modifier = Modifier.padding(end = 4.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Cyan5.copy(alpha = 0.15f),
                            labelColor = Cyan6,
                        ),
                    )
                }
            } else null,
        )

        Spacer(Modifier.height(8.dp))
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
