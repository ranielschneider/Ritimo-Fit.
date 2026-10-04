package com.ranielschneider.ritimofit.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainNavigationViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentAppRoute = MutableStateFlow<AppRoute>(AppRoute.Auth)
    val currentAppRoute: StateFlow<AppRoute> = _currentAppRoute.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collectLatest { user ->
                if (user == null) {
                    _currentAppRoute.value = AppRoute.Auth
                } else {
                    _currentAppRoute.value = when (user.role) {
                        UserRole.PROFESSOR -> AppRoute.Professor(ProfessorRoute.Dashboard)
                        UserRole.STUDENT -> AppRoute.Student(StudentRoute.Today())
                    }
                }
            }
        }
    }

    fun navigateToProfessorRoute(route: ProfessorRoute) {
        val current = _currentAppRoute.value
        if (current is AppRoute.Professor) {
            _currentAppRoute.value = AppRoute.Professor(route)
        }
    }

    fun navigateToStudentRoute(route: StudentRoute) {
        val current = _currentAppRoute.value
        if (current is AppRoute.Student) {
            _currentAppRoute.value = AppRoute.Student(route)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _currentAppRoute.value = AppRoute.Auth
        }
    }
}
