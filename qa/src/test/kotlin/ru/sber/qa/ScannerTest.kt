package ru.sber.qa

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ScannerTest {

    @Test
    fun `ensure scan timeouts for long duration`() {
        val scanner = Scanner(
            sleeper = NoOpSleeper(),
            scanDuration = { 20000 }
        )

        assertThrows(ScanTimeoutException::class.java) {
            scanner.getScanData()
        }
    }

    @Test
    fun `ensure scan completes normally for duration under 10s`() {
        val scanner = Scanner(
            sleeper = NoOpSleeper(),
            scanDuration = { 9999L }
        )

        assertDoesNotThrow {
            scanner.getScanData()
        }
    }
}