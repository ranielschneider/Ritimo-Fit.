package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.usecase.GetStudentDetailUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.navigation.StudentDetailTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StudentDetailData(
    val student: Student,
    val workouts: List<Workout> = emptyList(),
    val diet: Diet? = null
)

class StudentDetailViewModel(
    private val getStudentDetailUseCase: GetStudentDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StudentDetailData>>(UiState.Idle)
    val uiState: StateFlow<UiState<StudentDetailData>> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(StudentDetailTab.Overview)
    val selectedTab: StateFlow<StudentDetailTab> = _selectedTab.asStateFlow()

    private var currentStudentId: String? = null

    fun loadStudent(studentId: String) {
        if (currentStudentId == studentId && _uiState.value is UiState.Success) {
            return
        }
        currentStudentId = studentId

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getStudentDetailUseCase(studentId)) {
                is AppResult.Success -> {
                    _uiState.value = UiState.Success(
                        StudentDetailData(student = result.data)
                    )
                }
                is AppResult.Error -> {
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }

    fun onTabSelected(tab: StudentDetailTab) {
        _selectedTab.value = tab
    }
}
