package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @Test
    fun getScanDataWithScanTimeoutException() {
        every { Random.nextLong(5000L, 15000L) } returns 12000L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }
}