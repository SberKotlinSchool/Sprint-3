package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun `give mockk Random`() {
        //give
        mockkObject(Random)
    }

    @Test
    fun `ScanTimeoutException getScanData`() {
        //give
        mockkObject(Random)
        //when
        every { Random.nextLong(5000L, 15000L) } returns 11_000L
        //then
        assertThrows(ScanTimeoutException::class.java){
            Scanner.getScanData()
        }
    }

    @Test
    fun `positive getScanData`() {
        //when
        every { Random.nextLong(5000L, 15000L) } returns 1L
        //then
        assertDoesNotThrow{
            Scanner.getScanData()
        }
    }
}