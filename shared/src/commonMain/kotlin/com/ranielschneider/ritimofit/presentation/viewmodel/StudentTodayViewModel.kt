package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.usecase.GetStudentWorkoutAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutDetailUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class StudentTodayData(
    val studentUser: User,
    val assignedWorkouts: List<Workout> = emptyList()
)

class StudentTodayViewModel(
    private val authRepository: AuthRepository,
    private val getStudentWorkoutAssignmentsUseCase: GetStudentWorkoutAssignmentsUseCase,
    private val getWorkoutDetailUseCase: GetWorkoutDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StudentTodayData>>(UiState.Idle)
    val uiState: StateFlow<UiState<StudentTodayData>> = _uiState.asStateFlow()

    private val _readOnlyWorkoutState = MutableStateFlow<UiState<Workout>>(UiState.Idle)
    val readOnlyWorkoutState: StateFlow<UiState<Workout>> = _readOnlyWorkoutState.asStateFlow()

    init {
        loadTodayContent()
    }

    fun loadTodayContent() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val currentUserResult = authRepository.getCurrentSessionUser()
            val studentUser = when (currentUserResult) {
                is AppResult.Success -> currentUserResult.data
                is AppResult.Error -> {
                    User(
                        id = "std_1",
                        email = "aluno1@ritimofit.com",
                        fullName = "Carlos Aluno",
                        role = UserRole.STUDENT
                    )
                }
            }

            getStudentWorkoutAssignmentsUseCase(studentUser.id).collectLatest { result ->
                when (result) {
                    is AppResult.Success -> {
                        val workoutsList = result.data.map { it.workout }
                        _uiState.value = UiState.Success(
                            StudentTodayData(
                                studentUser = studentUser,
                                assignedWorkouts = workoutsList
                            )
                        )
                    }
                    is AppResult.Error -> {
                        _uiState.value = UiState.Success(
                            StudentTodayData(
                                studentUser = studentUser,
                                assignedWorkouts = emptyList()
                            )
                        )
                    }
                }
            }
        }
    }

    fun loadReadOnlyWorkout(workoutId: String) {
        viewModelScope.launch {
            _readOnlyWorkoutState.value = UiState.Loading
            when (val result = getWorkoutDetailUseCase(workoutId)) {
                is AppResult.Success -> {
                    _readOnlyWorkoutState.value = UiState.Success(result.data)
                }
                is AppResult.Error -> {
                    _readOnlyWorkoutState.value = UiState.Error(result.error)
                }
            }
        }
    }
}
