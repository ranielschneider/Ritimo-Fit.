package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.usecase.GetStudentDietAssignmentsUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StudentDietViewModel(
    private val authRepository: AuthRepository,
    private val getStudentDietAssignmentsUseCase: GetStudentDietAssignmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Diet?>>(UiState.Idle)
    val uiState: StateFlow<UiState<Diet?>> = _uiState.asStateFlow()

    init {
        loadStudentDiet()
    }

    fun loadStudentDiet() {
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

            getStudentDietAssignmentsUseCase(studentUser.id).collectLatest { result ->
                when (result) {
                    is AppResult.Success -> {
                        val activeDiet = result.data.firstOrNull()?.diet
                        _uiState.value = UiState.Success(activeDiet)
                    }
                    is AppResult.Error -> {
                        _uiState.value = UiState.Error(result.error)
                    }
                }
            }
        }
    }
}
