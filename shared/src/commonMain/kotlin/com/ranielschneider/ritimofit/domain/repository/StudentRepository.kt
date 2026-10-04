package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getStudentsForProfessor(professorId: String): Flow<AppResult<List<Student>>>
    suspend fun getStudentById(studentId: String): AppResult<Student>
    suspend fun addStudentToProfessor(professorId: String, student: Student): AppResult<Unit>
}
