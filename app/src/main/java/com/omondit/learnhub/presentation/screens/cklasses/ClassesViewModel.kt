package com.omondit.learnhub.presentation.screens.cklasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.model.CKlass
import com.omondit.learnhub.domain.usecase.content.GetClassesUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(
    private val getClassesUseCase: GetClassesUseCase
) : ViewModel() {

    private val _classesState = MutableStateFlow<UiState<List<CKlass>>>(UiState.Idle)
    val classesState: StateFlow<UiState<List<CKlass>>> = _classesState.asStateFlow()

    init {
        loadClasses()
    }

    fun loadClasses() {
        viewModelScope.launch {
            _classesState.value = UiState.Loading

            val result = getClassesUseCase()

            result.fold(
                onSuccess = { classes ->
                    _classesState.value = UiState.Success(classes)
                },
                onFailure = { exception ->
                    _classesState.value = UiState.Error(
                        exception.message ?: "Failed to load classes"
                    )
                }
            )
        }
    }
}
