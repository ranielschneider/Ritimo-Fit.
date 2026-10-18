package com.ranielschneider.ritimofit.presentation.student

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoIconButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoTertiaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.field.RitimoTextField
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.DocumentCategory
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDocumentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentDocumentsScreen(
    modifier: Modifier = Modifier,
    viewModel: StudentDocumentViewModel = koinViewModel()
) {
    val documentsState by viewModel.documentsUiState.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf<DocumentCategory?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = documentsState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando seus exames e documentos...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Erro ao carregar documentos",
                    message = "Não foi possível carregar sua lista de exames.",
                    onRetry = { viewModel.loadStudentDocuments() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val allDocs = state.data
                val filteredDocs = if (selectedCategoryFilter != null) {
                    allDocs.filter { it.category == selectedCategoryFilter }
                } else {
                    allDocs
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = RitimoTheme.spacing.lg)
                        .navigationBarsPadding()
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 1. Header
                    RitimoStatusBadge(
                        text = "ÁREA DO ALUNO",
                        type = BadgeType.PRIMARY
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    Text(
                        text = "Exames e Documentos",
                        style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                    Text(
                        text = "Envie laudos, exames laboratoriais e avaliações (PDF, JPG, PNG até 10 MB)",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 2. Action Button
                    RitimoPrimaryButton(
                        text = "+ Anexar Documento",
                        onClick = { showAddDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 3. Filter Category Bar
                    CategoryFilterRow(
                        selectedCategory = selectedCategoryFilter,
                        onSelectCategory = { selectedCategoryFilter = it }
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 4. Documents List
                    if (filteredDocs.isEmpty()) {
                        RitimoEmptyState(
                            title = "Nenhum documento encontrado",
                            description = "Toque em '+ Anexar Documento' para enviar seus laudos ou exames.",
                            icon = RitimoIcons.Documents,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                            filteredDocs.forEach { doc ->
                                StudentDocumentCard(
                                    document = doc,
                                    onOpen = { viewModel.openDocument(doc.fileUri) },
                                    onDelete = { viewModel.deleteDocument(doc.id) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
            else -> {}
        }

        // Add Document Modal Dialog
        if (showAddDialog) {
            AddDocumentDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, category, notes, onError ->
                    viewModel.pickAndUploadDocument(
                        title = title,
                        category = category,
                        notes = notes,
                        onSuccess = { showAddDialog = false },
                        onError = onError
                    )
                }
            )
        }
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: DocumentCategory?,
    onSelectCategory: (DocumentCategory?) -> Unit
) {
    val filters = listOf(
        null to "Todos",
        DocumentCategory.CLINICAL_AND_HORMONAL_LABS to "Laboratorial",
        DocumentCategory.IMAGING_EXAMS to "Imagem",
        DocumentCategory.PHYSICAL_ASSESSMENTS to "Avaliações",
        DocumentCategory.OTHER to "Outros"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(filters) { (cat, label) ->
            val isSelected = cat == selectedCategory
            val backgroundColor = if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface
            val contentColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
            val borderColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant

            Box(
                modifier = Modifier
                    .clip(RitimoTheme.shapes.pill)
                    .background(backgroundColor)
                    .border(1.dp, borderColor, RitimoTheme.shapes.pill)
                    .clickable { onSelectCategory(cat) }
                    .padding(horizontal = RitimoTheme.spacing.md, vertical = RitimoTheme.spacing.xs)
            ) {
                Text(
                    text = label,
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
}

@Composable
private fun StudentDocumentCard(
    document: StudentDocument,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sizeMb = (document.fileSizeBytes.toFloat() / (1024f * 1024f)).toString().take(3)

    RitimoGlassCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = document.title,
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onSurface
                )

                RitimoIconButton(
                    icon = RitimoIcons.Trash,
                    contentDescription = "Excluir documento",
                    onClick = onDelete
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Row(
                horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RitimoStatusBadge(
                    text = document.category.displayName.uppercase(),
                    type = BadgeType.PRIMARY
                )

                RitimoStatusBadge(
                    text = "${document.fileExtension.uppercase()} • $sizeMb MB",
                    type = BadgeType.NEUTRAL
                )
            }

            if (!document.notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                Text(
                    text = document.notes,
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            RitimoPrimaryButton(
                text = "Abrir documento",
                onClick = onOpen,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AddDocumentDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: DocumentCategory, notes: String?, onError: (String) -> Unit) -> Unit
) {
    var titleStr by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(DocumentCategory.CLINICAL_AND_HORMONAL_LABS) }
    var notesStr by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Anexar Exame / Documento",
                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                RitimoTextField(
                    value = titleStr,
                    onValueChange = {
                        titleStr = it
                        errorMsg = null
                    },
                    label = "Título do exame/documento",
                    placeholder = "Ex.: Hemograma completo 2024",
                    errorMessage = errorMsg
                )

                Text(
                    text = "Categoria:",
                    style = RitimoTheme.typography.labelSmall,
                    color = RitimoTheme.colors.onSurfaceVariant
                )

                // Category selector pills
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)) {
                    DocumentCategory.entries.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RitimoTheme.shapes.medium)
                                .background(if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface)
                                .border(1.dp, if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant, RitimoTheme.shapes.medium)
                                .clickable { selectedCategory = cat }
                                .padding(RitimoTheme.spacing.md)
                        ) {
                            Text(
                                text = cat.displayName,
                                style = RitimoTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurface
                            )
                        }
                    }
                }

                RitimoTextField(
                    value = notesStr,
                    onValueChange = { notesStr = it },
                    label = "Observações (Opcional)",
                    placeholder = "Ex.: Solicitado pelo médico dr. Silva"
                )
            }
        },
        confirmButton = {
            RitimoPrimaryButton(
                text = "Selecionar Arquivo e Enviar",
                onClick = {
                    if (titleStr.isNotBlank()) {
                        onConfirm(titleStr.trim(), selectedCategory, notesStr.trim().takeIf { it.isNotEmpty() }) { err ->
                            errorMsg = err
                        }
                    } else {
                        errorMsg = "Informe o título do documento."
                    }
                }
            )
        },
        dismissButton = {
            RitimoTertiaryButton(
                text = "Cancelar",
                onClick = onDismiss
            )
        }
    )
}
