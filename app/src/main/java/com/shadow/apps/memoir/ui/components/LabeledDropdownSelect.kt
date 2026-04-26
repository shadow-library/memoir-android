package com.shadow.apps.memoir.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadow.apps.memoir.ui.theme.Cyan6
import com.shadow.apps.memoir.ui.theme.Slate1
import com.shadow.apps.memoir.ui.theme.Slate6
import com.shadow.apps.memoir.ui.theme.Slate7

@Composable
fun <T> LabeledDropdownSelect(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    isLoading: Boolean = false,
    itemKey: (T) -> String,
    selectedLabel: (T?) -> String,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable (T) -> Unit)? = null,
) {
    val isDark = isSystemInDarkTheme()
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            onClick = { if (!isLoading && items.isNotEmpty()) expanded = true },
            enabled = !isLoading && items.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = if (isDark) Slate7 else Color.White,
            border = BorderStroke(width = 1.dp, color = if (isDark) Slate6 else Slate1),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (selectedItem != null && leadingContent != null) {
                    leadingContent(selectedItem)
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = selectedLabel(selectedItem),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedItem != null) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    },
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
            items.forEach { item ->
                val isSelected = selectedItem?.let { itemKey(it) } == itemKey(item)
                DropdownMenuItem(
                    leadingIcon = if (leadingContent != null) {
                        { leadingContent(item) }
                    } else null,
                    text = {
                        Text(
                            text = itemLabel(item),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) Cyan6 else MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                )
            }
        }
    }
}
