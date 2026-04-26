package com.shadow.apps.memoir.ui.main.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadow.apps.memoir.domain.model.Category
import com.shadow.apps.memoir.ui.components.LabeledDropdownSelect
import com.shadow.apps.memoir.ui.theme.Amber500
import com.shadow.apps.memoir.ui.theme.Cyan5
import com.shadow.apps.memoir.ui.theme.Emerald500
import com.shadow.apps.memoir.ui.theme.Slate4
import com.shadow.apps.memoir.ui.theme.Terracotta4
import com.shadow.apps.memoir.ui.theme.Violet500

private fun categoryColor(colorToken: String) = when (colorToken) {
    "emerald" -> Emerald500
    "amber" -> Amber500
    "cyan" -> Cyan5
    "violet" -> Violet500
    "terracotta" -> Terracotta4
    else -> Slate4
}

@Composable
fun CategorySelect(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedCategory = categories.firstOrNull { it.id == selectedCategoryId }

    LabeledDropdownSelect(
        items = categories,
        selectedItem = selectedCategory,
        onItemSelected = { onCategorySelected(it.id) },
        itemKey = { it.id },
        selectedLabel = { it?.name ?: "Select a category" },
        itemLabel = { it.name },
        leadingContent = { category ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(categoryColor(category.colorToken), CircleShape),
            )
        },
        modifier = modifier,
    )
}
