package com.ranielschneider.ritimofit.core.result

sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
}
