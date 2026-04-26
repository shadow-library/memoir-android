package com.shadow.apps.memoir.ui.main.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.apps.memoir.domain.usecase.record.AddDiaryEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddDiaryUiState(
    val entry: String = "",
    val tags: List<String> = emptyList(),
    val tagDraft: String = "",
    val date: LocalDate = LocalDate.now(),
    val isSaving: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val addDiaryEntryUseCase: AddDiaryEntryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddDiaryUiState())
    val uiState: StateFlow<AddDiaryUiState> = _uiState.asStateFlow()

    private val _savedEvent = MutableSharedFlow<Unit>()
    val savedEvent: SharedFlow<Unit> = _savedEvent.asSharedFlow()

    fun onEntryChange(value: String) = _uiState.update { it.copy(entry = value) }
    fun onTagDraftChange(value: String) = _uiState.update { it.copy(tagDraft = value) }
    fun onDateChange(date: LocalDate) = _uiState.update { it.copy(date = date) }

    fun addTag() {
        val draft = _uiState.value.tagDraft.trim()
        if (draft.isNotEmpty() && draft !in _uiState.value.tags) {
            _uiState.update { it.copy(tags = it.tags + draft, tagDraft = "") }
        }
    }

    fun removeTag(tag: String) = _uiState.update { it.copy(tags = it.tags - tag) }

    fun submit() {
        val state = _uiState.value
        if (state.entry.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val result = addDiaryEntryUseCase(
                content = state.entry,
                tags = state.tags,
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
}
