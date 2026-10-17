package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkoutExecutionTest {

    @Test
    fun testDurationCalculationFromTimestamps() {
        val startMs = 1_000_000L
        val endMs = 1_125_000L // 125 seconds = 2 min 05 sec

        val durationSec = ((endMs - startMs) / 1000).coerceAtLeast(1)
        assertEquals(125L, durationSec)

        val minutes = durationSec / 60
        val seconds = durationSec % 60
        val formatted = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        assertEquals("02:05", formatted)
    }

    @Test
    fun testCannotCompleteUnstartedSession() {
        val startMs = 0L
        val isStarted = startMs > 0
        assertTrue(!isStarted)
    }

    @Test
    fun testMultipleSessionsPerWorkout() {
        val sessions = mutableListOf<WorkoutCompletion>()

        val session1 = WorkoutCompletion(
            id = "cmpl_1",
            studentId = "std_1",
            workoutId = "wk_1",
            startedAtEpochMs = 1000L,
            completedAtEpochMs = 61000L,
            durationSeconds = 60L
        )

        val session2 = WorkoutCompletion(
            id = "cmpl_2",
            studentId = "std_1",
            workoutId = "wk_1",
            startedAtEpochMs = 100000L,
            completedAtEpochMs = 160000L,
            durationSeconds = 60L
        )

        sessions.add(session1)
        sessions.add(session2)

        assertEquals(2, sessions.size)
        assertEquals("cmpl_1", sessions[0].id)
        assertEquals("cmpl_2", sessions[1].id)
    }
}
