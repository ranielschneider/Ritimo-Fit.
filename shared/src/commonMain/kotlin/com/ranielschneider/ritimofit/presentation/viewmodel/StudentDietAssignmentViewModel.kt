package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.AssignedDietDetails
import com.ranielschneider.ritimofit.domain.usecase.AssignDietToStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentDietAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.RemoveDietAssignmentUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StudentDietAssignmentViewModel(
    private val getStudentDietAssignmentsUseCase: GetStudentDietAssignmentsUseCase,
    private val assignDietToStudentUseCase: AssignDietToStudentUseCase,
    private val removeDietAssignmentUseCase: RemoveDietAssignmentUseCase
) : ViewModel() {

    private val _assignedDietsUiState = MutableStateFlow<UiState<List<AssignedDietDetails>>>(UiState.Idle)
    val assignedDietsUiState: StateFlow<UiState<List<AssignedDietDetails>>> = _assignedDietsUiState.asStateFlow()

    private var currentStudentId: String? = null

    fun loadStudentAssignments(studentId: String) {
        currentStudentId = studentId
        viewModelScope.launch {
            _assignedDietsUiState.value = UiState.Loading
            getStudentDietAssignmentsUseCase(studentId).collectLatest { result ->
                _assignedDietsUiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun assignDiet(studentId: String, dietId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            when (assignDietToStudentUseCase(studentId, dietId)) {
                is AppResult.Success -> {
                    loadStudentAssignments(studentId)
                    onSuccess()
                }
                is AppResult.Error -> {}
            }
        }
    }

    fun replaceDietAssignment(studentId: String, newDietId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentAssignedList = (_assignedDietsUiState.value as? UiState.Success)?.data ?: emptyList()
            for (assignedDetails in currentAssignedList) {
                removeDietAssignmentUseCase(studentId, assignedDetails.diet.id)
            }
            when (assignDietToStudentUseCase(studentId, newDietId)) {
                is AppResult.Success -> {
                    loadStudentAssignments(studentId)
                    onSuccess()
                }
                is AppResult.Error -> {}
            }
        }
    }

    fun removeAssignment(studentId: String, dietId: String) {
        viewModelScope.launch {
            when (removeDietAssignmentUseCase(studentId, dietId)) {
                is AppResult.Success -> {
                    loadStudentAssignments(studentId)
                }
                is AppResult.Error -> {}
            }
        }
    }
}
