package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.sber.qa.Scanner.SCAN_TIMEOUT_THRESHOLD
import kotlin.random.Random

internal class ScannerTest {

    @AfterEach
    fun release() {
        unmockkAll()
    }

    @Test
    fun `подходящий таймаут`() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 5000L
        assertDoesNotThrow { Scanner.getScanData() }
    }

    @Test
    fun `неподходящий таймаут`() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns SCAN_TIMEOUT_THRESHOLD + 1
        assertThrows(ScanTimeoutException::class.java, { Scanner.getScanData() })
    }
}