package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @Test
    fun `ensure scan timeouts for long duration`() {
        every { Random.nextLong(any(), any()) } returns 15000

        assertThrows(ScanTimeoutException::class.java) {
            Scanner.getScanData()
        }
    }

    @Test
    fun `ensure scan completes normally for duration under 10s`() {
        every { Random.nextLong(any(), any()) } returns 7777

        assertDoesNotThrow {
            Scanner.getScanData()
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}