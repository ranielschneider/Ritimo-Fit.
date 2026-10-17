package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class WorkoutSessionProtectionTest {

    data class SessionState(
        val activeWorkoutId: String? = null,
        val sessionStartMs: Long = 0L,
        val completedSessions: List<WorkoutCompletion> = emptyList()
    )

    class SessionManager {
        var state = SessionState()
            private set

        fun tryStartSession(workoutId: String, currentMs: Long): String? {
            val currentActive = state.activeWorkoutId
            if (currentActive != null && currentActive != workoutId) {
                // Return active workoutId indicating conflict
                return currentActive
            }
            if (currentActive == null) {
                state = state.copy(
                    activeWorkoutId = workoutId,
                    sessionStartMs = currentMs
                )
            }
            return null
        }

        fun keepCurrentSession() {
            // Do nothing, preserve active session & start timestamp
        }

        fun switchSession(newWorkoutId: String, currentMs: Long, studentId: String = "std_1") {
            val currentActive = state.activeWorkoutId ?: return
            val startMs = state.sessionStartMs
            val durationSec = ((currentMs - startMs) / 1000).coerceAtLeast(1)

            val finishedCompletion = WorkoutCompletion(
                id = "cmpl_switch_$currentActive",
                studentId = studentId,
                workoutId = currentActive,
                startedAtEpochMs = startMs,
                completedAtEpochMs = currentMs,
                durationSeconds = durationSec
            )

            val updatedCompletions = state.completedSessions.toMutableList()
            updatedCompletions.add(finishedCompletion)

            state = SessionState(
                activeWorkoutId = newWorkoutId,
                sessionStartMs = currentMs,
                completedSessions = updatedCompletions
            )
        }
    }

    @Test
    fun testStartSessionWhenNoneActive() {
        val manager = SessionManager()
        val conflict = manager.tryStartSession("wk_peito", 1000L)

        assertNull(conflict)
        assertEquals("wk_peito", manager.state.activeWorkoutId)
        assertEquals(1000L, manager.state.sessionStartMs)
    }

    @Test
    fun testConflictDetectedWhenStartingSecondWorkout() {
        val manager = SessionManager()
        manager.tryStartSession("wk_peito", 1000L)

        // Attempt starting "wk_costas" while "wk_peito" is active
        val conflictActiveId = manager.tryStartSession("wk_costas", 5000L)

        assertNotNull(conflictActiveId)
        assertEquals("wk_peito", conflictActiveId)
        // Active session remains wk_peito started at 1000L
        assertEquals("wk_peito", manager.state.activeWorkoutId)
        assertEquals(1000L, manager.state.sessionStartMs)
    }

    @Test
    fun testCancelConflictKeepCurrentSession() {
        val manager = SessionManager()
        manager.tryStartSession("wk_peito", 1000L)

        val conflict = manager.tryStartSession("wk_costas", 5000L)
        assertNotNull(conflict)

        // User chooses "Continuar treino atual"
        manager.keepCurrentSession()

        assertEquals("wk_peito", manager.state.activeWorkoutId)
        assertEquals(1000L, manager.state.sessionStartMs)
        assertEquals(0, manager.state.completedSessions.size)
    }

    @Test
    fun testConfirmConflictSwitchSessionSavesHistoryAndStartsNew() {
        val manager = SessionManager()
        manager.tryStartSession("wk_peito", 10000L)

        val conflict = manager.tryStartSession("wk_costas", 70000L) // 60 seconds later
        assertNotNull(conflict)

        // User chooses "Finalizar atual e iniciar novo"
        manager.switchSession("wk_costas", 70000L)

        // 1. wk_peito is saved to completed sessions with 60s duration
        assertEquals(1, manager.state.completedSessions.size)
        val finishedSession = manager.state.completedSessions.first()
        assertEquals("wk_peito", finishedSession.workoutId)
        assertEquals(60L, finishedSession.durationSeconds)

        // 2. wk_costas is now active starting at 70000L
        assertEquals("wk_costas", manager.state.activeWorkoutId)
        assertEquals(70000L, manager.state.sessionStartMs)
    }

    @Test
    fun testStudentIsolation() {
        val managerCarlos = SessionManager()
        val managerMariana = SessionManager()

        managerCarlos.tryStartSession("wk_peito", 1000L)

        // Carlos active session does not affect Mariana
        assertNull(managerMariana.state.activeWorkoutId)
        val conflictMariana = managerMariana.tryStartSession("wk_costas", 2000L)
        assertNull(conflictMariana)
        assertEquals("wk_costas", managerMariana.state.activeWorkoutId)
    }
}
