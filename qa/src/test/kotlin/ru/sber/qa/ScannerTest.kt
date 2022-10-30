package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Random)
    }

    @Test
    fun `test getScanData correct output`() {
        //given
        val expectedSize = 100
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 1

        //when
        val scanData = Scanner.getScanData()

        //then
        assertEquals(expectedSize, scanData.size)
    }

    @Test
    fun `test getScanData throws ScanTimeoutException`() {
        //given
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        //when and then
        assertThrows(ScanTimeoutException::class.java) { Scanner.getScanData() }
    }
}