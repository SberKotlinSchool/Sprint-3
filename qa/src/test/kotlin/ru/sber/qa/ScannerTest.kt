package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ScannerTest {
    @AfterAll
    fun unmock(){
        unmockkAll()
    }

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