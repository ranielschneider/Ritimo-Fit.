package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.MealCompletion
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.usecase.CompleteMealUseCase
import com.ranielschneider.ritimofit.domain.usecase.CompleteWorkoutUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetMealCompletionsForStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentDietAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentWorkoutAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWaterTrackingUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutCompletionsForStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.UndoMealCompletionUseCase
import com.ranielschneider.ritimofit.domain.usecase.UpdateWaterConsumptionUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.time.Clock

data class StudentTodayData(
    val studentUser: User,
    val assignedWorkouts: List<Workout> = emptyList(),
    val completions: List<WorkoutCompletion> = emptyList(),
    val waterTracking: WaterTracking = WaterTracking(studentId = ""),
    val activeDiet: Diet? = null,
    val mealCompletions: List<MealCompletion> = emptyList()
)

data class WorkoutSessionConflict(
    val activeWorkoutId: String,
    val newWorkoutId: String
)

class StudentTodayViewModel(
    private val authRepository: AuthRepository,
    private val getStudentWorkoutAssignmentsUseCase: GetStudentWorkoutAssignmentsUseCase,
    private val getWorkoutDetailUseCase: GetWorkoutDetailUseCase,
    private val completeWorkoutUseCase: CompleteWorkoutUseCase,
    private val getWorkoutCompletionsForStudentUseCase: GetWorkoutCompletionsForStudentUseCase,
    private val getWaterTrackingUseCase: GetWaterTrackingUseCase,
    private val updateWaterConsumptionUseCase: UpdateWaterConsumptionUseCase,
    private val getStudentDietAssignmentsUseCase: GetStudentDietAssignmentsUseCase,
    private val completeMealUseCase: CompleteMealUseCase,
    private val undoMealCompletionUseCase: UndoMealCompletionUseCase,
    private val getMealCompletionsForStudentUseCase: GetMealCompletionsForStudentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StudentTodayData>>(UiState.Idle)
    val uiState: StateFlow<UiState<StudentTodayData>> = _uiState.asStateFlow()

    private val _readOnlyWorkoutState = MutableStateFlow<UiState<Workout>>(UiState.Idle)
    val readOnlyWorkoutState: StateFlow<UiState<Workout>> = _readOnlyWorkoutState.asStateFlow()

    // Workout execution session timer
    private val _activeWorkoutSessionId = MutableStateFlow<String?>(null)
    val activeWorkoutSessionId: StateFlow<String?> = _activeWorkoutSessionId.asStateFlow()

    private val _sessionStartTimestamp = MutableStateFlow<Long>(0L)
    val sessionStartTimestamp: StateFlow<Long> = _sessionStartTimestamp.asStateFlow()

    // Conflict detection
    private val _sessionConflict = MutableStateFlow<WorkoutSessionConflict?>(null)
    val sessionConflict: StateFlow<WorkoutSessionConflict?> = _sessionConflict.asStateFlow()

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

            combine(
                getStudentWorkoutAssignmentsUseCase(studentUser.id),
                getWorkoutCompletionsForStudentUseCase.observeCompletions(studentUser.id),
                getWaterTrackingUseCase(studentUser.id),
                getStudentDietAssignmentsUseCase(studentUser.id),
                getMealCompletionsForStudentUseCase.observeCompletions(studentUser.id)
            ) { workoutsRes, completionsRes, waterRes, dietRes, mealCompletionsRes ->
                val workouts = if (workoutsRes is AppResult.Success) workoutsRes.data.map { it.workout } else emptyList()
                val completions = if (completionsRes is AppResult.Success) completionsRes.data else emptyList()
                val water = if (waterRes is AppResult.Success) waterRes.data else WaterTracking(studentId = studentUser.id)
                val activeDiet = if (dietRes is AppResult.Success) dietRes.data.firstOrNull()?.diet else null
                val mealCompletions = if (mealCompletionsRes is AppResult.Success) mealCompletionsRes.data else emptyList()

                StudentTodayData(
                    studentUser = studentUser,
                    assignedWorkouts = workouts,
                    completions = completions,
                    waterTracking = water,
                    activeDiet = activeDiet,
                    mealCompletions = mealCompletions
                )
            }.collectLatest { todayData ->
                _uiState.value = UiState.Success(todayData)
            }
        }
    }

    fun startWorkoutSession(workoutId: String) {
        val currentActive = _activeWorkoutSessionId.value
        if (currentActive != null && currentActive != workoutId) {
            _sessionConflict.value = WorkoutSessionConflict(
                activeWorkoutId = currentActive,
                newWorkoutId = workoutId
            )
            return
        }
        if (currentActive == null) {
            _activeWorkoutSessionId.value = workoutId
            _sessionStartTimestamp.value = Clock.System.now().toEpochMilliseconds()
        }
    }

    fun dismissSessionConflict() {
        _sessionConflict.value = null
    }

    fun resolveConflictFinishAndStartNew(onSuccess: () -> Unit = {}) {
        val conflict = _sessionConflict.value ?: return
        val currentActive = conflict.activeWorkoutId
        val newWorkoutId = conflict.newWorkoutId
        val completedAt = Clock.System.now().toEpochMilliseconds()
        val startMs = _sessionStartTimestamp.value.takeIf { it > 0 } ?: (completedAt - 60000L)

        viewModelScope.launch {
            completeWorkoutUseCase(currentActive, startMs, completedAt)
            _activeWorkoutSessionId.value = newWorkoutId
            _sessionStartTimestamp.value = Clock.System.now().toEpochMilliseconds()
            _sessionConflict.value = null
            onSuccess()
        }
    }

    fun completeActiveWorkoutSession(workoutId: String, completedAtMs: Long = Clock.System.now().toEpochMilliseconds(), onSuccess: () -> Unit = {}) {
        val startMs = _sessionStartTimestamp.value.takeIf { it > 0 } ?: (completedAtMs - 60000L)
        viewModelScope.launch {
            completeWorkoutUseCase(workoutId, startMs, completedAtMs)
            _activeWorkoutSessionId.value = null
            _sessionStartTimestamp.value = 0L
            onSuccess()
        }
    }

    fun completeWorkout(workoutId: String) {
        val endMs = Clock.System.now().toEpochMilliseconds()
        val startMs = endMs - 60000L
        viewModelScope.launch {
            completeWorkoutUseCase(workoutId, startMs, endMs)
        }
    }

    fun completeMeal(dietId: String, mealId: String) {
        viewModelScope.launch {
            completeMealUseCase(dietId, mealId)
        }
    }

    fun undoMealCompletion(dietId: String, mealId: String) {
        viewModelScope.launch {
            undoMealCompletionUseCase(dietId, mealId)
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
