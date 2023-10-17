package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class ScannerTest {
    @BeforeEach
    private fun init() {
        mockkObject(Random)

    }

    @Test
    fun getScanDataTestSuccess() {

        val byteArray = ByteArray(10)

        every { Random.nextLong(5000L, 15000L) } returns 100L
        every { Random.nextBytes(100) } returns byteArray

        assertEquals(byteArray, Scanner.getScanData())
    }

    @Test
    fun getScanDataThrowsTimeoutException() {
        every { Random.nextLong(5000L, 15000L) } returns 14000L

        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }
}