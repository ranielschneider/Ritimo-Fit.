package com.ranielschneider.ritimofit.di

import com.ranielschneider.ritimofit.data.datasource.AuthDataSource
import com.ranielschneider.ritimofit.data.datasource.DietDataSource
import com.ranielschneider.ritimofit.data.datasource.ExerciseDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryAuthDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryDietDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryExerciseDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryStudentDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryWorkoutAssignmentDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryWorkoutCompletionDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryWorkoutDataSource
import com.ranielschneider.ritimofit.data.datasource.InMemoryWaterTrackingDataSource
import com.ranielschneider.ritimofit.data.datasource.StudentDataSource
import com.ranielschneider.ritimofit.data.datasource.WorkoutAssignmentDataSource
import com.ranielschneider.ritimofit.data.datasource.WorkoutCompletionDataSource
import com.ranielschneider.ritimofit.data.datasource.WorkoutDataSource
import com.ranielschneider.ritimofit.data.datasource.WaterTrackingDataSource
import com.ranielschneider.ritimofit.data.repository.AuthRepositoryImpl
import com.ranielschneider.ritimofit.data.repository.DietRepositoryImpl
import com.ranielschneider.ritimofit.data.repository.ExerciseRepositoryImpl
import com.ranielschneider.ritimofit.data.repository.StudentRepositoryImpl
import com.ranielschneider.ritimofit.data.repository.WorkoutAssignmentRepositoryImpl
import com.ranielschneider.ritimofit.data.repository.WorkoutCompletionRepositoryImpl
import com.ranielschneider.ritimofit.data.repository.WorkoutRepositoryImpl
import com.ranielschneider.ritimofit.data.repository.WaterTrackingRepositoryImpl
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.DietRepository
import com.ranielschneider.ritimofit.domain.repository.ExerciseRepository
import com.ranielschneider.ritimofit.domain.repository.StudentRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutAssignmentRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutCompletionRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository
import com.ranielschneider.ritimofit.domain.repository.WaterTrackingRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthDataSource> { InMemoryAuthDataSource() }
    single<StudentDataSource> { InMemoryStudentDataSource() }
    single<WorkoutDataSource> { InMemoryWorkoutDataSource() }
    single<ExerciseDataSource> { InMemoryExerciseDataSource() }
    single<DietDataSource> { InMemoryDietDataSource() }
    single<WorkoutAssignmentDataSource> { InMemoryWorkoutAssignmentDataSource() }
    single<WorkoutCompletionDataSource> { InMemoryWorkoutCompletionDataSource() }
    single<WaterTrackingDataSource> { InMemoryWaterTrackingDataSource() }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<StudentRepository> { StudentRepositoryImpl(get(), get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get(), get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get(), get()) }
    single<DietRepository> { DietRepositoryImpl(get(), get()) }
    single<WorkoutAssignmentRepository> { WorkoutAssignmentRepositoryImpl(get(), get()) }
    single<WorkoutCompletionRepository> { WorkoutCompletionRepositoryImpl(get(), get()) }
    single<WaterTrackingRepository> { WaterTrackingRepositoryImpl(get(), get()) }
}
