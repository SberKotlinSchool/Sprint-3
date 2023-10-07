package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

class ScannerTest {
    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @Test
    fun getScanData() {
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 1
        val data = Scanner.getScanData()
        assertEquals(100, data.size)
    }

    @Test
    fun getScanDataScanTimeoutException() {
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1
        assertThrows(ScanTimeoutException::class.java) { Scanner.getScanData() }
    }
}