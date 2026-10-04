package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.model.WorkoutExercise
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutsForProfessorUseCase
import com.ranielschneider.ritimofit.domain.usecase.SaveWorkoutUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val getWorkoutsForProfessorUseCase: GetWorkoutsForProfessorUseCase,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val getWorkoutDetailUseCase: GetWorkoutDetailUseCase
) : ViewModel() {

    // 1. Workout List State
    private val _workoutsUiState = MutableStateFlow<UiState<List<Workout>>>(UiState.Idle)
    val workoutsUiState: StateFlow<UiState<List<Workout>>> = _workoutsUiState.asStateFlow()

    // 2. Editor State
    private val _currentWorkoutId = MutableStateFlow<String?>(null)
    val currentWorkoutId: StateFlow<String?> = _currentWorkoutId.asStateFlow()

    private val _workoutTitle = MutableStateFlow("")
    val workoutTitle: StateFlow<String> = _workoutTitle.asStateFlow()

    private val _workoutDescription = MutableStateFlow("")
    val workoutDescription: StateFlow<String> = _workoutDescription.asStateFlow()

    private val _workoutExercises = MutableStateFlow<List<WorkoutExercise>>(emptyList())
    val workoutExercises: StateFlow<List<WorkoutExercise>> = _workoutExercises.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    init {
        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            _workoutsUiState.value = UiState.Loading
            getWorkoutsForProfessorUseCase().collectLatest { result ->
                _workoutsUiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun initNewWorkout() {
        _currentWorkoutId.value = null
        _workoutTitle.value = ""
        _workoutDescription.value = ""
        _workoutExercises.value = emptyList()
        _saveError.value = null
        _isSaving.value = false
    }

    fun loadWorkoutForEditing(workoutId: String) {
        viewModelScope.launch {
            _isSaving.value = true
            when (val result = getWorkoutDetailUseCase(workoutId)) {
                is AppResult.Success -> {
                    val workout = result.data
                    _currentWorkoutId.value = workout.id
                    _workoutTitle.value = workout.title
                    _workoutDescription.value = workout.description.orEmpty()
                    _workoutExercises.value = workout.exercises
                    _saveError.value = null
                }
                is AppResult.Error -> {
                    _saveError.value = "Não foi possível carregar o treino para edição."
                }
            }
            _isSaving.value = false
        }
    }

    fun onTitleChanged(title: String) {
        _workoutTitle.value = title
        _saveError.value = null
    }

    fun onDescriptionChanged(description: String) {
        _workoutDescription.value = description
    }

    fun addExerciseFromCatalog(exercise: Exercise) {
        val currentList = _workoutExercises.value.toMutableList()
        val nextOrder = currentList.size
        val uniqueId = "we_" + (100000..999999).random() + "_" + exercise.id
        val newWorkoutExercise = WorkoutExercise(
            id = uniqueId,
            exerciseId = exercise.id,
            exerciseName = exercise.name,
            muscleGroup = exercise.muscleGroup,
            equipment = exercise.equipment,
            order = nextOrder,
            sets = 4,
            reps = "12",
            weightKg = null,
            restSeconds = 60,
            notes = null
        )
        currentList.add(newWorkoutExercise)
        _workoutExercises.value = currentList
    }

    fun updateExerciseConfig(
        index: Int,
        sets: Int,
        reps: String,
        weightKg: Double?,
        restSeconds: Int,
        notes: String?
    ) {
        val currentList = _workoutExercises.value.toMutableList()
        if (index in currentList.indices) {
            val item = currentList[index]
            currentList[index] = item.copy(
                sets = sets.coerceAtLeast(1),
                reps = reps,
                weightKg = weightKg,
                restSeconds = restSeconds.coerceAtLeast(0),
                notes = notes
            )
            _workoutExercises.value = currentList
        }
    }

    fun moveExerciseUp(index: Int) {
        if (index > 0) {
            val currentList = _workoutExercises.value.toMutableList()
            val temp = currentList[index]
            currentList[index] = currentList[index - 1]
            currentList[index - 1] = temp

            // Re-assign orders
            val reordered = currentList.mapIndexed { i, item -> item.copy(order = i) }
            _workoutExercises.value = reordered
        }
    }

    fun moveExerciseDown(index: Int) {
        val currentList = _workoutExercises.value.toMutableList()
        if (index < currentList.size - 1) {
            val temp = currentList[index]
            currentList[index] = currentList[index + 1]
            currentList[index + 1] = temp

            // Re-assign orders
            val reordered = currentList.mapIndexed { i, item -> item.copy(order = i) }
            _workoutExercises.value = reordered
        }
    }

    fun removeExercise(index: Int) {
        val currentList = _workoutExercises.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            val reordered = currentList.mapIndexed { i, item -> item.copy(order = i) }
            _workoutExercises.value = reordered
        }
    }

    fun saveWorkout(onSuccess: () -> Unit) {
        val title = _workoutTitle.value.trim()
        if (title.isEmpty()) {
            _saveError.value = "O nome do treino é obrigatório."
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null

            val id = _currentWorkoutId.value ?: ("wk_" + (1000..9999).random())
            val workout = Workout(
                id = id,
                professorId = "", // Will be filled by SaveWorkoutUseCase from current session
                title = title,
                description = _workoutDescription.value.trim().takeIf { it.isNotEmpty() },
                exercises = _workoutExercises.value,
                createdAtEpochMs = 0L
            )

            when (saveWorkoutUseCase(workout)) {
                is AppResult.Success -> {
                    _isSaving.value = false
                    loadWorkouts()
                    onSuccess()
                }
                is AppResult.Error -> {
                    _isSaving.value = false
                    _saveError.value = "Erro ao salvar treino. Tente novamente."
                }
            }
        }
    }
}
