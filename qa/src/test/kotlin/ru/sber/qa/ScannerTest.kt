package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random.Default)
    }

    @Test
    fun getScanTimeoutException() {
        every { Random.Default.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 5000L
        Assertions.assertThrows(ScanTimeoutException::class.java) { Scanner.getScanData() }
    }

    @Test
    fun getScanDataSuccessfully() {
        every { Random.Default.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD
        Assertions.assertDoesNotThrow { Scanner.getScanData() }
    }
}