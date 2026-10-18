package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.DefaultFilePickerHandler
import com.ranielschneider.ritimofit.domain.model.DocumentCategory
import com.ranielschneider.ritimofit.domain.model.FilePickerHandler
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.usecase.AddStudentDocumentUseCase
import com.ranielschneider.ritimofit.domain.usecase.DeleteStudentDocumentUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentDocumentsUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Clock

class StudentDocumentViewModel(
    private val authRepository: AuthRepository,
    private val getStudentDocumentsUseCase: GetStudentDocumentsUseCase,
    private val addStudentDocumentUseCase: AddStudentDocumentUseCase,
    private val deleteStudentDocumentUseCase: DeleteStudentDocumentUseCase,
    private val filePickerHandler: FilePickerHandler = DefaultFilePickerHandler()
) : ViewModel() {

    private val _documentsUiState = MutableStateFlow<UiState<List<StudentDocument>>>(UiState.Idle)
    val documentsUiState: StateFlow<UiState<List<StudentDocument>>> = _documentsUiState.asStateFlow()

    private var currentStudentId: String? = null

    init {
        loadStudentDocuments()
    }

    fun loadStudentDocuments(targetStudentId: String? = null) {
        viewModelScope.launch {
            _documentsUiState.value = UiState.Loading

            val studentId = if (targetStudentId != null) {
                targetStudentId
            } else {
                when (val userResult = authRepository.getCurrentSessionUser()) {
                    is AppResult.Success -> userResult.data.id
                    is AppResult.Error -> "std_1"
                }
            }
            currentStudentId = studentId

            getStudentDocumentsUseCase(studentId).collectLatest { result ->
                _documentsUiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun pickAndUploadDocument(
        title: String,
        category: DocumentCategory,
        notes: String? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val studentId = currentStudentId ?: return
        viewModelScope.launch {
            when (val fileResult = filePickerHandler.pickDocument()) {
                is AppResult.Success -> {
                    val file = fileResult.data
                    val nowMs = Clock.System.now().toEpochMilliseconds()
                    when (val addResult = addStudentDocumentUseCase(
                        studentId = studentId,
                        title = title,
                        category = category,
                        fileUri = file.uri,
                        fileName = file.name,
                        fileExtension = file.extension,
                        fileSizeBytes = file.sizeBytes,
                        examDateEpochMs = nowMs,
                        notes = notes
                    )) {
                        is AppResult.Success -> onSuccess()
                        is AppResult.Error -> {
                            val errorMsg = when (val err = addResult.error) {
                                is AppError.PermissionDenied -> err.message
                                is AppError.NotFound -> err.resourceName
                                else -> "Erro ao adicionar documento."
                            }
                            onError(errorMsg)
                        }
                    }
                }
                is AppResult.Error -> onError("Erro ao selecionar arquivo.")
            }
        }
    }

    fun deleteDocument(documentId: String) {
        val studentId = currentStudentId ?: return
        viewModelScope.launch {
            deleteStudentDocumentUseCase(studentId, documentId)
        }
    }

    fun openDocument(fileUri: String) {
        filePickerHandler.openDocument(fileUri)
    }
}
