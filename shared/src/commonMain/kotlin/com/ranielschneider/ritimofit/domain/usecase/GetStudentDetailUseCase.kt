package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.domain.repository.StudentRepository

class GetStudentDetailUseCase(
    private val studentRepository: StudentRepository
) {
    suspend operator fun invoke(studentId: String): AppResult<Student> {
        return studentRepository.getStudentById(studentId)
    }
}
