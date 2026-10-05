package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.usecase.CompleteWorkoutUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentWorkoutAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWaterTrackingUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutCompletionsForStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.UpdateWaterConsumptionUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class StudentTodayData(
    val studentUser: User,
    val assignedWorkouts: List<Workout> = emptyList(),
    val completions: List<WorkoutCompletion> = emptyList(),
    val waterTracking: WaterTracking = WaterTracking(studentId = "")
)

class StudentTodayViewModel(
    private val authRepository: AuthRepository,
    private val getStudentWorkoutAssignmentsUseCase: GetStudentWorkoutAssignmentsUseCase,
    private val getWorkoutDetailUseCase: GetWorkoutDetailUseCase,
    private val completeWorkoutUseCase: CompleteWorkoutUseCase,
    private val getWorkoutCompletionsForStudentUseCase: GetWorkoutCompletionsForStudentUseCase,
    private val getWaterTrackingUseCase: GetWaterTrackingUseCase,
    private val updateWaterConsumptionUseCase: UpdateWaterConsumptionUseCase
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

            // Observe water tracking
            launch {
                getWaterTrackingUseCase(studentUser.id).collectLatest { waterResult ->
                    val water = if (waterResult is AppResult.Success) waterResult.data else WaterTracking(studentId = studentUser.id)
                    val currentSuccess = _uiState.value as? UiState.Success<StudentTodayData>
                    if (currentSuccess != null) {
                        _uiState.value = UiState.Success(
                            currentSuccess.data.copy(waterTracking = water)
                        )
                    }
                }
            }

            // Observe completions
            launch {
                getWorkoutCompletionsForStudentUseCase.observeCompletions(studentUser.id).collectLatest { completionsResult ->
                    val completions = if (completionsResult is AppResult.Success) completionsResult.data else emptyList()
                    val currentSuccess = _uiState.value as? UiState.Success<StudentTodayData>
                    if (currentSuccess != null) {
                        _uiState.value = UiState.Success(
                            currentSuccess.data.copy(completions = completions)
                        )
                    }
                }
            }

            getStudentWorkoutAssignmentsUseCase(studentUser.id).collectLatest { result ->
                when (result) {
                    is AppResult.Success -> {
                        val workoutsList = result.data.map { it.workout }
                        val currentSuccess = _uiState.value as? UiState.Success<StudentTodayData>
                        val currentCompletions = currentSuccess?.data?.completions ?: emptyList()
                        val currentWater = currentSuccess?.data?.waterTracking ?: WaterTracking(studentId = studentUser.id)
                        _uiState.value = UiState.Success(
                            StudentTodayData(
                                studentUser = studentUser,
                                assignedWorkouts = workoutsList,
                                completions = currentCompletions,
                                waterTracking = currentWater
                            )
                        )
                    }
                    is AppResult.Error -> {
                        _uiState.value = UiState.Success(
                            StudentTodayData(
                                studentUser = studentUser,
                                assignedWorkouts = emptyList(),
                                completions = emptyList(),
                                waterTracking = WaterTracking(studentId = studentUser.id)
                            )
                        )
                    }
                }
            }
        }
    }

    fun completeWorkout(workoutId: String) {
        val currentState = (_uiState.value as? UiState.Success<StudentTodayData>)?.data ?: return
        viewModelScope.launch {
            completeWorkoutUseCase(workoutId, currentState.completions)
        }
    }

    fun addWater(amountMl: Int) {
        val currentState = (_uiState.value as? UiState.Success<StudentTodayData>)?.data ?: return
        viewModelScope.launch {
            updateWaterConsumptionUseCase.addWater(currentState.studentUser.id, amountMl)
        }
    }

    fun removeWater(amountMl: Int) {
        val currentState = (_uiState.value as? UiState.Success<StudentTodayData>)?.data ?: return
        viewModelScope.launch {
            updateWaterConsumptionUseCase.removeWater(currentState.studentUser.id, amountMl)
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
