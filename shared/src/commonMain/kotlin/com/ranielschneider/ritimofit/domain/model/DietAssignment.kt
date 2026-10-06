package com.ranielschneider.ritimofit.domain.model

data class DietAssignment(
    val id: String,
    val studentId: String,
    val dietId: String,
    val assignedAtEpochMs: Long = 0L
)

data class AssignedDietDetails(
    val assignmentId: String,
    val studentId: String,
    val diet: Diet
)
