package com.shadow.apps.memoir.ui.main.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.apps.memoir.domain.model.Category
import com.shadow.apps.memoir.domain.model.EnabledCurrency
import com.shadow.apps.memoir.domain.usecase.record.AddExpenseUseCase
import com.shadow.apps.memoir.domain.usecase.record.ObserveCategoriesUseCase
import com.shadow.apps.memoir.domain.usecase.record.ObserveEnabledCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddExpenseUiState(
    val amount: String = "",
    val currency: String = "",
    val merchant: String = "",
    val selectedCategoryId: String? = null,
    val note: String = "",
    val date: LocalDate = LocalDate.now(),
    val currencies: List<EnabledCurrency> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoadingCurrencies: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val observeEnabledCurrenciesUseCase: ObserveEnabledCurrenciesUseCase,
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    private val _savedEvent = MutableSharedFlow<Unit>()
    val savedEvent: SharedFlow<Unit> = _savedEvent.asSharedFlow()

    init {
        observeCurrencies()
        observeCategories()
    }

    fun onAmountChange(value: String) = _uiState.update { it.copy(amount = value) }
    fun onCurrencyChange(code: String) = _uiState.update { it.copy(currency = code) }
    fun onMerchantChange(value: String) = _uiState.update { it.copy(merchant = value) }
    fun onCategoryChange(id: String) = _uiState.update { it.copy(selectedCategoryId = id) }
    fun onNoteChange(value: String) = _uiState.update { it.copy(note = value) }
    fun onDateChange(date: LocalDate) = _uiState.update { it.copy(date = date) }

    fun submit() {
        val state = _uiState.value
        val amount = state.amount.toDoubleOrNull() ?: return
        val categoryId = state.selectedCategoryId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val result = addExpenseUseCase(
                amount = amount,
                currency = state.currency,
                merchant = state.merchant,
                categoryId = categoryId,
                note = state.note,
                date = state.date,
            )
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSaving = false) }
                    _savedEvent.emit(Unit)
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isSaving = false, error = e.localizedMessage) }
                },
            )
        }
    }

    private fun observeCurrencies() {
        observeEnabledCurrenciesUseCase()
            .onEach { currencies ->
                _uiState.update { state ->
                    val nextCurrency = when {
                        state.currency.isNotBlank() && currencies.any { it.code == state.currency } -> state.currency
                        else -> currencies.firstOrNull { it.isDefault }?.code
                            ?: currencies.firstOrNull()?.code
                            ?: ""
                    }
                    state.copy(
                        currencies = currencies,
                        currency = nextCurrency,
                        isLoadingCurrencies = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCategories() {
        observeCategoriesUseCase()
            .onEach { categories -> _uiState.update { it.copy(categories = categories) } }
            .launchIn(viewModelScope)
    }
}
