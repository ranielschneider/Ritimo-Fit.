package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentDataSource {
    fun observeStudentsForProfessor(professorId: String): Flow<AppResult<List<Student>>>
    suspend fun getStudentById(studentId: String): AppResult<Student>
    suspend fun addStudentToProfessor(professorId: String, student: Student): AppResult<Unit>
}
