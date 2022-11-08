package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun init() {
        mockkObject(Random)
    }

    @Test
    fun getScanDataScanTimeoutException() {
        every { Random.nextLong(any(), any()) } returns 12345L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun getScanDataScanSuccess() {
        val result = byteArrayOf(100)
        every { Random.nextLong(any(), any()) } returns 1000L
        every { Random.nextBytes(100) } returns result
        val actual = Scanner.getScanData()
        assertEquals(result, actual)
    }
}