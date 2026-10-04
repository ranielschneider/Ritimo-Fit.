package com.ranielschneider.ritimofit.presentation.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ranielschneider.ritimofit.presentation.auth.AuthContainerScreen
import com.ranielschneider.ritimofit.presentation.professor.ProfessorAppShell
import com.ranielschneider.ritimofit.presentation.student.StudentAppShell
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainRootScreen(
    modifier: Modifier = Modifier,
    viewModel: MainNavigationViewModel = koinViewModel()
) {
    val currentAppRoute by viewModel.currentAppRoute.collectAsState()

    AnimatedContent(
        targetState = currentAppRoute,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = modifier
    ) { route ->
        when (route) {
            is AppRoute.Auth -> {
                AuthContainerScreen()
            }
            is AppRoute.Professor -> {
                ProfessorAppShell(
                    currentRoute = route.route,
                    viewModel = viewModel
                )
            }
            is AppRoute.Student -> {
                StudentAppShell(
                    currentRoute = route.route,
                    viewModel = viewModel
                )
            }
        }
    }
}
