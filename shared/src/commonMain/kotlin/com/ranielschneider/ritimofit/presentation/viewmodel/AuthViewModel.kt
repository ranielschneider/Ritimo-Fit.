package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.usecase.GetCurrentUserUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AuthViewModel(
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val currentUserState: StateFlow<UiState<User>> = getCurrentUserUseCase()
        .map { user ->
            if (user != null) {
                UiState.Success(user)
            } else {
                UiState.Idle
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )
}
