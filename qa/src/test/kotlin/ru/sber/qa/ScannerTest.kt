package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ScannerTest {

    @Test
    fun `ScanTimeoutException getScanData`() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 11_000L
        assertThrows(ScanTimeoutException::class.java){
            Scanner.getScanData()
        }
    }
    @Test
    fun `positive getScanData`() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 1L
        assertDoesNotThrow{
            Scanner.getScanData()
        }
    }
}