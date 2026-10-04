package com.ranielschneider.ritimofit.presentation.base

import com.ranielschneider.ritimofit.core.result.AppError

sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val error: AppError, val message: String? = null) : UiState<Nothing>
}
