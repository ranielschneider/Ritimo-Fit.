package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryStudentDataSource : StudentDataSource {
    private val studentsState = MutableStateFlow<List<Student>>(
        listOf(
            Student(
                userId = "std_1",
                user = User(
                    id = "std_1",
                    email = "aluno1@ritimofit.com",
                    fullName = "Carlos Aluno",
                    role = UserRole.STUDENT
                ),
                professorId = "prof_123",
                heightCm = 178.0,
                weightKg = 75.5,
                goals = "Hipertrofia"
            ),
            Student(
                userId = "std_2",
                user = User(
                    id = "std_2",
                    email = "aluno2@ritimofit.com",
                    fullName = "Mariana Aluna",
                    role = UserRole.STUDENT
                ),
                professorId = "prof_123",
                heightCm = 165.0,
                weightKg = 60.0,
                goals = "Emagrecimento"
            )
        )
    )

    override fun observeStudentsForProfessor(professorId: String): Flow<AppResult<List<Student>>> {
        return studentsState.map { list ->
            val filtered = list.filter { it.professorId == professorId }
            AppResult.Success(filtered)
        }
    }

    override suspend fun getStudentById(studentId: String): AppResult<Student> {
        val student = studentsState.value.find { it.userId == studentId }
        return if (student != null) {
            AppResult.Success(student)
        } else {
            AppResult.Error(AppError.NotFound("Aluno não encontrado"))
        }
    }

    override suspend fun addStudentToProfessor(professorId: String, student: Student): AppResult<Unit> {
        val currentList = studentsState.value.toMutableList()
        currentList.add(student.copy(professorId = professorId))
        studentsState.value = currentList
        return AppResult.Success(Unit)
    }
}
