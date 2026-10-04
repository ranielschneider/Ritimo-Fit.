package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.domain.usecase.GetExerciseCatalogUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetExerciseDetailUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val getExerciseCatalogUseCase: GetExerciseCatalogUseCase,
    private val getExerciseDetailUseCase: GetExerciseDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Exercise>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Exercise>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _exerciseDetailState = MutableStateFlow<UiState<Exercise>>(UiState.Idle)
    val exerciseDetailState: StateFlow<UiState<Exercise>> = _exerciseDetailState.asStateFlow()

    val filteredUiState: StateFlow<UiState<List<Exercise>>> = combine(
        _uiState,
        _searchQuery,
        _selectedCategory
    ) { state, query, category ->
        if (state is UiState.Success) {
            val trimmedQuery = query.trim()
            val filtered = state.data.filter { exercise ->
                val matchesCategory = when (category) {
                    "Todos" -> true
                    "Pernas" -> exercise.muscleGroup.equals("Pernas", ignoreCase = true) ||
                                exercise.muscleGroup.equals("Quadríceps", ignoreCase = true) ||
                                exercise.muscleGroup.equals("Posterior de coxa", ignoreCase = true) ||
                                exercise.muscleGroup.equals("Panturrilhas", ignoreCase = true)
                    "Ombros" -> exercise.muscleGroup.equals("Ombros", ignoreCase = true) ||
                                exercise.muscleGroup.equals("Trapézio", ignoreCase = true)
                    "Bíceps" -> exercise.muscleGroup.equals("Bíceps", ignoreCase = true) ||
                                exercise.muscleGroup.equals("Antebraço", ignoreCase = true)
                    else -> exercise.muscleGroup.equals(category, ignoreCase = true)
                }

                val matchesQuery = if (trimmedQuery.isEmpty()) {
                    true
                } else {
                    exercise.name.contains(trimmedQuery, ignoreCase = true) ||
                    exercise.muscleGroup.contains(trimmedQuery, ignoreCase = true) ||
                    (exercise.equipment?.contains(trimmedQuery, ignoreCase = true) == true)
                }

                matchesCategory && matchesQuery
            }
            UiState.Success(filtered)
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Idle
    )

    init {
        loadCatalog()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getExerciseCatalogUseCase().collectLatest { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun loadExerciseDetail(exerciseId: String) {
        viewModelScope.launch {
            _exerciseDetailState.value = UiState.Loading
            when (val result = getExerciseDetailUseCase(exerciseId)) {
                is AppResult.Success -> {
                    _exerciseDetailState.value = UiState.Success(result.data)
                }
                is AppResult.Error -> {
                    _exerciseDetailState.value = UiState.Error(result.error)
                }
            }
        }
    }

    fun clearExerciseDetailState() {
        _exerciseDetailState.value = UiState.Idle
    }
}
