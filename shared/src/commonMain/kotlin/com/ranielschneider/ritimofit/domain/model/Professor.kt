package com.ranielschneider.ritimofit.domain.model

data class Professor(
    val userId: String,
    val user: User,
    val specialty: String? = null,
    val bio: String? = null,
    val studentIds: List<String> = emptyList()
)
