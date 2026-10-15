package com.ranielschneider.ritimofit.di

import com.ranielschneider.ritimofit.domain.usecase.AssignDietToStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.AssignWorkoutToStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.CompleteMealUseCase
import com.ranielschneider.ritimofit.domain.usecase.CompleteWorkoutUseCase
import com.ranielschneider.ritimofit.domain.usecase.DeleteDietUseCase
import com.ranielschneider.ritimofit.domain.usecase.ForgotPasswordUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetCurrentUserUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetDietDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetDietsForProfessorUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetExerciseCatalogUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetExerciseDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetMealCompletionsForStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentDietAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentWorkoutAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentWorkoutsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentsForProfessorUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWaterTrackingUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutCompletionsForStudentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutsForProfessorUseCase
import com.ranielschneider.ritimofit.domain.usecase.LoginUseCase
import com.ranielschneider.ritimofit.domain.usecase.RegisterUserUseCase
import com.ranielschneider.ritimofit.domain.usecase.RemoveDietAssignmentUseCase
import com.ranielschneider.ritimofit.domain.usecase.RemoveWorkoutAssignmentUseCase
import com.ranielschneider.ritimofit.domain.usecase.SaveDietUseCase
import com.ranielschneider.ritimofit.domain.usecase.SaveWorkoutUseCase
import com.ranielschneider.ritimofit.domain.usecase.SearchFoodsUseCase
import com.ranielschneider.ritimofit.domain.usecase.UndoMealCompletionUseCase
import com.ranielschneider.ritimofit.domain.usecase.UpdateWaterConsumptionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCurrentUserUseCase(get()) }
    factory { GetStudentsForProfessorUseCase(get(), get()) }
    factory { GetStudentDetailUseCase(get()) }
    factory { GetStudentWorkoutsUseCase(get(), get(), get()) }
    factory { GetExerciseCatalogUseCase(get()) }
    factory { GetExerciseDetailUseCase(get()) }
    factory { GetWorkoutsForProfessorUseCase(get(), get()) }
    factory { SaveWorkoutUseCase(get(), get()) }
    factory { GetWorkoutDetailUseCase(get()) }
    factory { GetStudentWorkoutAssignmentsUseCase(get(), get()) }
    factory { AssignWorkoutToStudentUseCase(get()) }
    factory { RemoveWorkoutAssignmentUseCase(get()) }
    factory { CompleteWorkoutUseCase(get(), get()) }
    factory { GetWorkoutCompletionsForStudentUseCase(get(), get()) }
    factory { GetWaterTrackingUseCase(get()) }
    factory { UpdateWaterConsumptionUseCase(get()) }
    factory { GetDietsForProfessorUseCase(get(), get()) }
    factory { SaveDietUseCase(get(), get()) }
    factory { GetDietDetailUseCase(get()) }
    factory { DeleteDietUseCase(get()) }
    factory { GetStudentDietAssignmentsUseCase(get(), get()) }
    factory { AssignDietToStudentUseCase(get()) }
    factory { RemoveDietAssignmentUseCase(get()) }
    factory { CompleteMealUseCase(get(), get()) }
    factory { UndoMealCompletionUseCase(get(), get()) }
    factory { GetMealCompletionsForStudentUseCase(get()) }
    factory { SearchFoodsUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    factory { RegisterUserUseCase(get()) }
}
