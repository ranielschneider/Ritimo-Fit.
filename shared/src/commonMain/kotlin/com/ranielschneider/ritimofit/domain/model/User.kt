package com.ranielschneider.ritimofit.domain.model

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val role: UserRole,
    val avatarUrl: String? = null,
    val createdAtEpochMs: Long = 0L
)
