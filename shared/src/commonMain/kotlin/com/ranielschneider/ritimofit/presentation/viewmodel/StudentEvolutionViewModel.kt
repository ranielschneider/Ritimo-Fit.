package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.CompletedWorkoutDetails
import com.ranielschneider.ritimofit.domain.model.EvolutionSummary
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.usecase.AddBodyMeasurementUseCase
import com.ranielschneider.ritimofit.domain.usecase.AddWeightRecordUseCase
import com.ranielschneider.ritimofit.domain.usecase.DeleteWeightRecordUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentEvolutionUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutCompletionsForStudentUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Clock

class StudentEvolutionViewModel(
    private val authRepository: AuthRepository,
    private val getStudentEvolutionUseCase: GetStudentEvolutionUseCase,
    private val addWeightRecordUseCase: AddWeightRecordUseCase,
    private val deleteWeightRecordUseCase: DeleteWeightRecordUseCase,
    private val addBodyMeasurementUseCase: AddBodyMeasurementUseCase,
    private val getWorkoutCompletionsForStudentUseCase: GetWorkoutCompletionsForStudentUseCase
) : ViewModel() {

    private val _evolutionUiState = MutableStateFlow<UiState<EvolutionSummary>>(UiState.Idle)
    val evolutionUiState: StateFlow<UiState<EvolutionSummary>> = _evolutionUiState.asStateFlow()

    private val _completedWorkoutsState = MutableStateFlow<UiState<List<CompletedWorkoutDetails>>>(UiState.Idle)
    val completedWorkoutsState: StateFlow<UiState<List<CompletedWorkoutDetails>>> = _completedWorkoutsState.asStateFlow()

    private var currentStudentId: String? = null

    init {
        loadStudentEvolution()
    }

    fun loadStudentEvolution(targetStudentId: String? = null) {
        viewModelScope.launch {
            _evolutionUiState.value = UiState.Loading
            _completedWorkoutsState.value = UiState.Loading

            val studentId = if (targetStudentId != null) {
                targetStudentId
            } else {
                when (val userResult = authRepository.getCurrentSessionUser()) {
                    is AppResult.Success -> userResult.data.id
                    is AppResult.Error -> "std_1"
                }
            }
            currentStudentId = studentId

            // Observe weight and measurements summary
            launch {
                getStudentEvolutionUseCase(studentId).collectLatest { result ->
                    _evolutionUiState.value = when (result) {
                        is AppResult.Success -> UiState.Success(result.data)
                        is AppResult.Error -> UiState.Error(result.error)
                    }
                }
            }

            // Observe completed workout details for real workout history
            launch {
                getWorkoutCompletionsForStudentUseCase.observeCompletedDetails(studentId).collectLatest { result ->
                    _completedWorkoutsState.value = when (result) {
                        is AppResult.Success -> UiState.Success(result.data)
                        is AppResult.Error -> UiState.Error(result.error)
                    }
                }
            }
        }
    }

    fun addWeightRecord(weightKg: Double, notes: String? = null, onSuccess: () -> Unit = {}) {
        val studentId = currentStudentId ?: return
        viewModelScope.launch {
            val dateEpochMs = Clock.System.now().toEpochMilliseconds()
            when (addWeightRecordUseCase(studentId, weightKg, dateEpochMs, notes)) {
                is AppResult.Success -> {
                    onSuccess()
                }
                is AppResult.Error -> {}
            }
        }
    }

    fun deleteWeightRecord(recordId: String) {
        val studentId = currentStudentId ?: return
        viewModelScope.launch {
            deleteWeightRecordUseCase(studentId, recordId)
        }
    }

    fun addMeasurementRecord(
        waistCm: Double?,
        abdomenCm: Double?,
        chestCm: Double?,
        armCm: Double?,
        thighCm: Double?,
        hipCm: Double?,
        notes: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        val studentId = currentStudentId ?: return
        viewModelScope.launch {
            val dateEpochMs = Clock.System.now().toEpochMilliseconds()
            when (addBodyMeasurementUseCase(
                studentId = studentId,
                dateEpochMs = dateEpochMs,
                waistCm = waistCm,
                abdomenCm = abdomenCm,
                chestCm = chestCm,
                armCm = armCm,
                thighCm = thighCm,
                hipCm = hipCm,
                notes = notes
            )) {
                is AppResult.Success -> {
                    onSuccess()
                }
                is AppResult.Error -> {}
            }
        }
    }
}
