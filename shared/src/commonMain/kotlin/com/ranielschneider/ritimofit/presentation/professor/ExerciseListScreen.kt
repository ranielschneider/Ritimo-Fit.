package com.ranielschneider.ritimofit.presentation.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
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
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.ExerciseViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExerciseListScreen(
    onSelectExercise: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseViewModel = koinViewModel()
) {
    val rawUiState by viewModel.uiState.collectAsState()
    val filteredUiState by viewModel.filteredUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val categories = listOf(
        "Todos", "Peito", "Costas", "Ombros", "Bíceps", "Tríceps", "Pernas", "Glúteos", "Abdômen", "Cardio"
    )

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = RitimoTheme.spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 1. Header Section
            RitimoStatusBadge(
                text = "PLANEJAMENTO • CATÁLOGO",
                type = BadgeType.PRIMARY
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = "Exercícios",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Explore e organize seu catálogo de exercícios",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Search Section
            RitimoSearchField(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeholder = "Buscar exercício",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // 3. Category Filter Chips (LazyRow)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    val backgroundColor = if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface
                    val contentColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
                    val borderColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant

                    Box(
                        modifier = Modifier
                            .clip(RitimoTheme.shapes.pill)
                            .background(backgroundColor)
                            .border(1.dp, borderColor, RitimoTheme.shapes.pill)
                            .clickable { viewModel.onCategorySelected(category) }
                            .padding(horizontal = RitimoTheme.spacing.md, vertical = RitimoTheme.spacing.xs)
                    ) {
                        Text(
                            text = category,
                            style = RitimoTheme.typography.titleMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = contentColor,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // 4. Content Section (Loading / Empty / List / Error)
            when (val state = filteredUiState) {
                is UiState.Loading -> {
                    RitimoLoadingState(
                        message = "Carregando catálogo de exercícios...",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Error -> {
                    RitimoErrorState(
                        title = "Erro ao carregar catálogo",
                        message = "Não foi possível carregar os exercícios.",
                        onRetry = { viewModel.loadCatalog() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Success -> {
                    val filteredList = state.data
                    val isRawEmpty = rawUiState is UiState.Success && (rawUiState as UiState.Success<List<Exercise>>).data.isEmpty()

                    if (isRawEmpty) {
                        RitimoEmptyState(
                            title = "Nenhum exercício cadastrado",
                            description = "O catálogo está vazio no momento.",
                            icon = RitimoIcons.Workouts,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (filteredList.isEmpty()) {
                        RitimoEmptyState(
                            title = "Nenhum exercício encontrado",
                            description = "Nenhum exercício corresponde ao filtro de categoria ou à busca \"$searchQuery\".",
                            icon = RitimoIcons.Search,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = filteredList,
                                key = { exercise -> exercise.id }
                            ) { exercise ->
                                ExerciseItemCard(
                                    exercise = exercise,
                                    onClick = { onSelectExercise(exercise.id) },
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
private fun ExerciseItemCard(
    exercise: Exercise,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            // Exercise Category Icon Container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RitimoTheme.shapes.medium)
                    .background(RitimoTheme.colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = RitimoIcons.Workouts,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = RitimoTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            // Exercise Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)
                ) {
                    Text(
                        text = exercise.muscleGroup,
                        style = RitimoTheme.typography.bodySmall,
                        color = RitimoTheme.colors.primary
                    )

                    if (exercise.equipment != null) {
                        Text(
                            text = "•",
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                        Text(
                            text = exercise.equipment,
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

            Icon(
                imageVector = RitimoIcons.ChevronRight,
                contentDescription = "Ver detalhes do exercício ${exercise.name}",
                modifier = Modifier.size(20.dp),
                tint = RitimoTheme.colors.onSurfaceVariant
            )
        }
    }
}
