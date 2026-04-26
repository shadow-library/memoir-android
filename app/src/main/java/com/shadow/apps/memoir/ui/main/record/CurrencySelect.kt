package com.shadow.apps.memoir.ui.main.record

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shadow.apps.memoir.domain.model.EnabledCurrency
import com.shadow.apps.memoir.ui.components.LabeledDropdownSelect

@Composable
fun CurrencySelect(
    currencies: List<EnabledCurrency>,
    selectedCode: String,
    onCurrencySelected: (String) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val selectedOption = currencies.firstOrNull { it.code == selectedCode }

    LabeledDropdownSelect(
        items = currencies,
        selectedItem = selectedOption,
        onItemSelected = { onCurrencySelected(it.code) },
        isLoading = isLoading,
        itemKey = { it.code },
        selectedLabel = { it?.label ?: selectedCode },
        itemLabel = { it.label },
        modifier = modifier,
    )
}
