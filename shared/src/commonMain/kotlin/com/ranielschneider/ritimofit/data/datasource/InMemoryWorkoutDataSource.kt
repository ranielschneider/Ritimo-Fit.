package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.model.WorkoutExercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryWorkoutDataSource : WorkoutDataSource {
    private val workoutsState = MutableStateFlow<List<Workout>>(
        listOf(
            Workout(
                id = "wk_peito_triceps",
                professorId = "prof_123",
                title = "Treino A — Peito e Tríceps",
                description = "Foco em peitoral superior e hipertrofia de tríceps",
                exercises = listOf(
                    WorkoutExercise(
                        id = "we_1",
                        exerciseId = "ex_peito_01",
                        exerciseName = "Supino Reto com Barra",
                        muscleGroup = "Peito",
                        equipment = "Barra e Banco Reto",
                        order = 0,
                        sets = 4,
                        reps = "10-12",
                        weightKg = 30.0,
                        restSeconds = 90
                    ),
                    WorkoutExercise(
                        id = "we_2",
                        exerciseId = "ex_peito_02",
                        exerciseName = "Supino Inclinado com Halteres",
                        muscleGroup = "Peito",
                        equipment = "Halteres e Banco Inclinado",
                        order = 1,
                        sets = 4,
                        reps = "12",
                        weightKg = 18.0,
                        restSeconds = 60
                    ),
                    WorkoutExercise(
                        id = "we_3",
                        exerciseId = "ex_triceps_01",
                        exerciseName = "Tríceps Pulley com Barra Reta",
                        muscleGroup = "Tríceps",
                        equipment = "Cabo/polia",
                        order = 2,
                        sets = 3,
                        reps = "15",
                        weightKg = 25.0,
                        restSeconds = 45
                    ),
                    WorkoutExercise(
                        id = "we_4",
                        exerciseId = "ex_triceps_02",
                        exerciseName = "Tríceps Corda no Pulley",
                        muscleGroup = "Tríceps",
                        equipment = "Cabo/polia",
                        order = 3,
                        sets = 3,
                        reps = "12",
                        weightKg = 20.0,
                        restSeconds = 45
                    )
                )
            ),
            Workout(
                id = "wk_costas_biceps",
                professorId = "prof_123",
                title = "Treino B — Costas e Bíceps",
                description = "Desenvolvimento de dorsais e largura de braços",
                exercises = listOf(
                    WorkoutExercise(
                        id = "we_5",
                        exerciseId = "ex_costas_03",
                        exerciseName = "Puxada Frontal no Pulley Pegada Aberta",
                        muscleGroup = "Costas",
                        equipment = "Máquina / Cabo",
                        order = 0,
                        sets = 4,
                        reps = "12",
                        weightKg = 45.0,
                        restSeconds = 60
                    ),
                    WorkoutExercise(
                        id = "we_6",
                        exerciseId = "ex_costas_05",
                        exerciseName = "Remada Curvada com Barra",
                        muscleGroup = "Costas",
                        equipment = "Barra Livre",
                        order = 1,
                        sets = 4,
                        reps = "10",
                        weightKg = 35.0,
                        restSeconds = 60
                    ),
                    WorkoutExercise(
                        id = "we_7",
                        exerciseId = "ex_biceps_01",
                        exerciseName = "Rosca Direta com Barra W",
                        muscleGroup = "Bíceps",
                        equipment = "Barra W",
                        order = 2,
                        sets = 3,
                        reps = "12",
                        weightKg = 14.0,
                        restSeconds = 45
                    ),
                    WorkoutExercise(
                        id = "we_8",
                        exerciseId = "ex_biceps_03",
                        exerciseName = "Rosca Martelo com Halteres",
                        muscleGroup = "Bíceps",
                        equipment = "Halteres",
                        order = 3,
                        sets = 3,
                        reps = "12",
                        weightKg = 12.0,
                        restSeconds = 45
                    )
                )
            )
        )
    )

    override fun observeWorkoutsForStudent(studentId: String): Flow<AppResult<List<Workout>>> {
        return workoutsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }
            AppResult.Success(filtered)
        }
    }

    override fun observeWorkoutsForProfessor(professorId: String): Flow<AppResult<List<Workout>>> {
        return workoutsState.map { list ->
            val filtered = list.filter { it.professorId == professorId }
            AppResult.Success(filtered)
        }
    }

    override suspend fun getWorkoutById(workoutId: String): AppResult<Workout> {
        val workout = workoutsState.value.find { it.id == workoutId }
        return if (workout != null) {
            AppResult.Success(workout)
        } else {
            AppResult.Error(AppError.NotFound("Treino não encontrado"))
        }
    }

    override suspend fun saveWorkout(workout: Workout): AppResult<Unit> {
        val currentList = workoutsState.value.toMutableList()
        currentList.removeAll { it.id == workout.id }
        currentList.add(workout)
        workoutsState.value = currentList
        return AppResult.Success(Unit)
    }

    override suspend fun deleteWorkout(workoutId: String): AppResult<Unit> {
        val currentList = workoutsState.value.toMutableList()
        currentList.removeAll { it.id == workoutId }
        workoutsState.value = currentList
        return AppResult.Success(Unit)
    }
}
