package com.ranielschneider.ritimofit.presentation.professor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.usecase.GetCurrentUserUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardSummary(
    val activeStudents: String = "—",
    val pendingAttention: String = "—",
    val todayWorkouts: String = "—",
    val adherenceRate: String = "—"
)

data class ProfessorDashboardData(
    val professorName: String?,
    val summary: DashboardSummary = DashboardSummary()
)

class ProfessorDashboardViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ProfessorDashboardData>>(UiState.Idle)
    val uiState: StateFlow<UiState<ProfessorDashboardData>> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val userResult = getCurrentUserUseCase.getCurrentSessionUser()) {
                is AppResult.Success -> {
                    val user = userResult.data
                    val data = ProfessorDashboardData(
                        professorName = user.fullName
                    )
                    _uiState.value = UiState.Success(data)
                }
                is AppResult.Error -> {
                    val data = ProfessorDashboardData(
                        professorName = null
                    )
                    _uiState.value = UiState.Success(data)
                }
            }
        }
    }
}
