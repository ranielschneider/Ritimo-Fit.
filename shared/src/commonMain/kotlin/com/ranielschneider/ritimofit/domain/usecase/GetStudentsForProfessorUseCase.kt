package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetStudentsForProfessorUseCase(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository
) {
    suspend operator fun invoke(): Flow<AppResult<List<Student>>> {
        val currentUserResult = authRepository.getCurrentSessionUser()
        if (currentUserResult is AppResult.Error) {
            return flowOf(AppResult.Error(currentUserResult.error))
        }

        val currentUser = (currentUserResult as AppResult.Success).data

        if (currentUser.role != UserRole.PROFESSOR) {
            return flowOf(AppResult.Error(AppError.Auth.Unauthorized("Apenas professores podem acessar lista de alunos")))
        }

        return studentRepository.getStudentsForProfessor(currentUser.id)
    }

    suspend operator fun invoke(requestedProfessorId: String): Flow<AppResult<List<Student>>> {
        val currentUserResult = authRepository.getCurrentSessionUser()
        if (currentUserResult is AppResult.Error) {
            return flowOf(AppResult.Error(currentUserResult.error))
        }

        val currentUser = (currentUserResult as AppResult.Success).data

        if (currentUser.role != UserRole.PROFESSOR) {
            return flowOf(AppResult.Error(AppError.Auth.Unauthorized("Apenas professores podem acessar lista de alunos")))
        }

        if (currentUser.id != requestedProfessorId) {
            return flowOf(AppResult.Error(AppError.PermissionDenied("Um professor só pode acessar seus próprios alunos")))
        }

        return studentRepository.getStudentsForProfessor(requestedProfessorId)
    }
}
