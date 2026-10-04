package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.domain.usecase.GetStudentsForProfessorUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentListViewModel(
    private val getStudentsForProfessorUseCase: GetStudentsForProfessorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Student>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Student>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredUiState: StateFlow<UiState<List<Student>>> = combine(
        _uiState,
        _searchQuery
    ) { state, query ->
        if (state is UiState.Success) {
            val trimmedQuery = query.trim()
            if (trimmedQuery.isEmpty()) {
                state
            } else {
                val filteredList = state.data.filter { student ->
                    student.user.fullName.contains(trimmedQuery, ignoreCase = true)
                }
                UiState.Success(filteredList)
            }
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Idle
    )

    init {
        loadStudents()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun loadStudents(professorId: String? = null) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val flow = if (professorId != null) {
                getStudentsForProfessorUseCase(professorId)
            } else {
                getStudentsForProfessorUseCase()
            }
            flow.collectLatest { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }
}
