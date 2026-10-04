package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.AssignedWorkoutDetails
import com.ranielschneider.ritimofit.domain.usecase.AssignWorkoutToStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentWorkoutAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.RemoveWorkoutAssignmentUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StudentWorkoutAssignmentViewModel(
    private val getStudentWorkoutAssignmentsUseCase: GetStudentWorkoutAssignmentsUseCase,
    private val assignWorkoutToStudentUseCase: AssignWorkoutToStudentUseCase,
    private val removeWorkoutAssignmentUseCase: RemoveWorkoutAssignmentUseCase
) : ViewModel() {

    private val _assignedWorkoutsUiState = MutableStateFlow<UiState<List<AssignedWorkoutDetails>>>(UiState.Idle)
    val assignedWorkoutsUiState: StateFlow<UiState<List<AssignedWorkoutDetails>>> = _assignedWorkoutsUiState.asStateFlow()

    private var currentStudentId: String? = null

    fun loadStudentAssignments(studentId: String) {
        currentStudentId = studentId
        viewModelScope.launch {
            _assignedWorkoutsUiState.value = UiState.Loading
            getStudentWorkoutAssignmentsUseCase(studentId).collectLatest { result ->
                _assignedWorkoutsUiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun assignWorkout(studentId: String, workoutId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            when (assignWorkoutToStudentUseCase(studentId, workoutId)) {
                is AppResult.Success -> {
                    loadStudentAssignments(studentId)
                    onSuccess()
                }
                is AppResult.Error -> {}
            }
        }
    }

    fun removeAssignment(studentId: String, workoutId: String) {
        viewModelScope.launch {
            when (removeWorkoutAssignmentUseCase(studentId, workoutId)) {
                is AppResult.Success -> {
                    loadStudentAssignments(studentId)
                }
                is AppResult.Error -> {}
            }
        }
    }
}
