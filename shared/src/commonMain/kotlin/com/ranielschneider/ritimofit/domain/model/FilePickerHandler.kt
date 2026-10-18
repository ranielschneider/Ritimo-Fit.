package com.ranielschneider.ritimofit.domain.model

import com.ranielschneider.ritimofit.core.result.AppResult

data class SelectedFileResult(
    val uri: String,
    val name: String,
    val extension: String,
    val sizeBytes: Long
)

interface FilePickerHandler {
    suspend fun pickDocument(): AppResult<SelectedFileResult>
    fun openDocument(fileUri: String): AppResult<Unit>
}

class DefaultFilePickerHandler : FilePickerHandler {
    override suspend fun pickDocument(): AppResult<SelectedFileResult> {
        return AppResult.Success(
            SelectedFileResult(
                uri = "content://ritimofit/docs/exame_laboratorial_" + (1000..9999).random() + ".pdf",
                name = "exame_laboratorial_" + (1000..9999).random() + ".pdf",
                extension = "pdf",
                sizeBytes = 2_100_000L
            )
        )
    }

    override fun openDocument(fileUri: String): AppResult<Unit> {
        return AppResult.Success(Unit)
    }
}
