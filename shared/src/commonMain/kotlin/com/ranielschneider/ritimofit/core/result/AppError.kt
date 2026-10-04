package com.ranielschneider.ritimofit.core.result

sealed interface AppError {
    data class Network(val cause: Throwable? = null) : AppError
    sealed interface Auth : AppError {
        data object Unauthenticated : Auth
        data object InvalidCredentials : Auth
        data class Unauthorized(val reason: String = "Acesso não autorizado") : Auth
    }
    data class PermissionDenied(val message: String = "Acesso negado a este recurso") : AppError
    data class NotFound(val resourceName: String) : AppError
    data class Unknown(val cause: Throwable? = null) : AppError
}
