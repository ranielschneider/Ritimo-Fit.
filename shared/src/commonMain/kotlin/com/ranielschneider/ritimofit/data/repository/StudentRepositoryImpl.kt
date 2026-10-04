package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.StudentDataSource
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class StudentRepositoryImpl(
    private val studentDataSource: StudentDataSource,
    private val dispatchers: AppDispatchers
) : StudentRepository {

    override fun getStudentsForProfessor(professorId: String): Flow<AppResult<List<Student>>> {
        return studentDataSource.observeStudentsForProfessor(professorId).flowOn(dispatchers.io)
    }

    override suspend fun getStudentById(studentId: String): AppResult<Student> {
        return withContext(dispatchers.io) {
            studentDataSource.getStudentById(studentId)
        }
    }

    override suspend fun addStudentToProfessor(professorId: String, student: Student): AppResult<Unit> {
        return withContext(dispatchers.io) {
            studentDataSource.addStudentToProfessor(professorId, student)
        }
    }
}
