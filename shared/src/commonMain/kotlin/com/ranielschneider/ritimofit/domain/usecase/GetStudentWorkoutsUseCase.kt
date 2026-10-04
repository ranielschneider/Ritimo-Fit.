package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.StudentRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetStudentWorkoutsUseCase(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository,
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(targetStudentId: String): Flow<AppResult<List<Workout>>> {
        val currentUserResult = authRepository.getCurrentSessionUser()
        if (currentUserResult is AppResult.Error) {
            return flowOf(AppResult.Error(currentUserResult.error))
        }

        val currentUser = (currentUserResult as AppResult.Success).data

        when (currentUser.role) {
            UserRole.STUDENT -> {
                if (currentUser.id != targetStudentId) {
                    return flowOf(AppResult.Error(AppError.PermissionDenied("Um aluno só pode visualizar seus próprios treinos")))
                }
            }
            UserRole.PROFESSOR -> {
                val studentResult = studentRepository.getStudentById(targetStudentId)
                if (studentResult is AppResult.Error) {
                    return flowOf(AppResult.Error(studentResult.error))
                }
                val student = (studentResult as AppResult.Success).data
                if (student.professorId != currentUser.id) {
                    return flowOf(AppResult.Error(AppError.PermissionDenied("Você só pode visualizar treinos de seus próprios alunos")))
                }
            }
        }

        return workoutRepository.getWorkoutsForStudent(targetStudentId)
    }
}
