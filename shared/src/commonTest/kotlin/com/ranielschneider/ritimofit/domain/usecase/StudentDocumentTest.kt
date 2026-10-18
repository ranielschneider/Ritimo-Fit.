package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.domain.model.DocumentCategory
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StudentDocumentTest {

    private val allowedExtensions = AddStudentDocumentUseCase.ALLOWED_EXTENSIONS
    private val maxSizeBytes = AddStudentDocumentUseCase.MAX_FILE_SIZE_BYTES

    @Test
    fun testFileExtensionValidation() {
        assertTrue(allowedExtensions.contains("pdf"))
        assertTrue(allowedExtensions.contains("jpg"))
        assertTrue(allowedExtensions.contains("jpeg"))
        assertTrue(allowedExtensions.contains("png"))

        assertFalse(allowedExtensions.contains("exe"))
        assertFalse(allowedExtensions.contains("mp4"))
        assertFalse(allowedExtensions.contains("txt"))
    }

    @Test
    fun testMaxFileSizeValidation() {
        val validSize = 5 * 1024 * 1024L // 5 MB
        val invalidExcessiveSize = 15 * 1024 * 1024L // 15 MB

        assertTrue(validSize in 1..maxSizeBytes)
        assertFalse(invalidExcessiveSize <= maxSizeBytes)
    }

    @Test
    fun testAddAndRemoveDocumentRecords() {
        val documents = mutableListOf(
            StudentDocument(
                id = "doc_1",
                studentId = "std_1",
                title = "Hemograma",
                category = DocumentCategory.CLINICAL_AND_HORMONAL_LABS,
                fileUri = "content://docs/hemo.pdf",
                fileName = "hemo.pdf",
                fileExtension = "pdf",
                fileSizeBytes = 2_000_000L
            )
        )

        assertEquals(1, documents.size)

        // Add new document
        val doc2 = StudentDocument(
            id = "doc_2",
            studentId = "std_1",
            title = "Raio X",
            category = DocumentCategory.IMAGING_EXAMS,
            fileUri = "content://docs/raiox.png",
            fileName = "raiox.png",
            fileExtension = "png",
            fileSizeBytes = 1_500_000L
        )
        documents.add(doc2)
        assertEquals(2, documents.size)

        // Delete document
        documents.removeAll { it.id == "doc_1" }
        assertEquals(1, documents.size)
        assertEquals("doc_2", documents.first().id)
    }

    @Test
    fun testStudentDocumentIsolation() {
        val allDocs = listOf(
            StudentDocument(
                id = "doc_1",
                studentId = "std_1",
                title = "Doc Carlos",
                category = DocumentCategory.CLINICAL_AND_HORMONAL_LABS,
                fileUri = "uri1",
                fileName = "f1.pdf",
                fileExtension = "pdf",
                fileSizeBytes = 1_000_000L
            ),
            StudentDocument(
                id = "doc_2",
                studentId = "std_2",
                title = "Doc Mariana",
                category = DocumentCategory.IMAGING_EXAMS,
                fileUri = "uri2",
                fileName = "f2.png",
                fileExtension = "png",
                fileSizeBytes = 1_000_000L
            )
        )

        val std1Docs = allDocs.filter { it.studentId == "std_1" }
        val std2Docs = allDocs.filter { it.studentId == "std_2" }

        assertEquals(1, std1Docs.size)
        assertEquals("Doc Carlos", std1Docs.first().title)

        assertEquals(1, std2Docs.size)
        assertEquals("Doc Mariana", std2Docs.first().title)
    }
}
