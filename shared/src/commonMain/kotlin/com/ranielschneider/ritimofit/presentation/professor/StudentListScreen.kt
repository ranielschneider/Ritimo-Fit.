package com.ranielschneider.ritimofit.presentation.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.field.RitimoSearchField
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentListScreen(
    onSelectStudent: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentListViewModel = koinViewModel()
) {
    val rawUiState by viewModel.uiState.collectAsState()
    val filteredUiState by viewModel.filteredUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = RitimoTheme.spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 1. Header Section
            RitimoStatusBadge(
                text = "GESTÃO DE ALUNOS",
                type = BadgeType.PRIMARY
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = "Alunos",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Gerencie e acompanhe seus alunos",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Search Section
            RitimoSearchField(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeholder = "Buscar aluno",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // 3. Content Section (Loading / Empty / Search Empty / Error / List)
            when (val state = filteredUiState) {
                is UiState.Loading -> {
                    RitimoLoadingState(
                        message = "Carregando seus alunos...",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Error -> {
                    RitimoErrorState(
                        title = "Erro ao carregar alunos",
                        message = "Não foi possível carregar a lista de alunos.",
                        onRetry = { viewModel.loadStudents() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Success -> {
                    val filteredList = state.data
                    val isRawEmpty = rawUiState is UiState.Success && (rawUiState as UiState.Success<List<Student>>).data.isEmpty()

                    if (isRawEmpty) {
                        RitimoEmptyState(
                            title = "Nenhum aluno ainda",
                            description = "Quando você adicionar alunos, eles aparecerão aqui.",
                            icon = RitimoIcons.User,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (filteredList.isEmpty()) {
                        RitimoEmptyState(
                            title = "Nenhum aluno encontrado",
                            description = "Nenhum aluno corresponde à busca \"$searchQuery\".",
                            icon = RitimoIcons.Search,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = filteredList,
                                key = { student -> student.userId }
                            ) { student ->
                                StudentItemCard(
                                    student = student,
                                    onClick = { onSelectStudent(student.userId) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = RitimoTheme.spacing.xs)
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun StudentItemCard(
    student: Student,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initial = student.user.fullName.trim().firstOrNull()?.uppercase() ?: "A"
    val secondaryInfo = student.goals?.takeIf { it.isNotBlank() }?.let { "Objetivo: $it" } ?: student.user.email

    RitimoGlassCard(
        modifier = modifier,
        onClick = onClick,
        padding = RitimoTheme.spacing.md
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar Initial Circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RitimoTheme.shapes.pill)
                    .background(RitimoTheme.colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.primary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            // Student Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.user.fullName,
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = secondaryInfo,
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

            // Clickable Chevron Indicator
            Icon(
                imageVector = RitimoIcons.ChevronRight,
                contentDescription = "Ver detalhes de ${student.user.fullName}",
                modifier = Modifier.size(20.dp),
                tint = RitimoTheme.colors.onSurfaceVariant
            )
        }
    }
}
