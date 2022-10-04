package ru.sber.qa.test

import io.mockk.*
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import ru.sber.qa.*
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getScanData() {
        // given
        val duration = Scanner.SCAN_TIMEOUT_THRESHOLD - 100L
        every { Random.nextLong(any(), any()) } returns duration

        // when
        val result = Scanner.getScanData()

        // then
        verify { Random.nextLong(any(), any()) }
        assertEquals(result.size, 100)
        assertTrue(result is ByteArray)
        verify { Random.nextBytes(size = any()) }
    }

    @Test
    fun getScanDataWithException() {
        // given
        val duration = Scanner.SCAN_TIMEOUT_THRESHOLD + 100L
        every { Random.nextLong(any(), any()) } returns duration

        // when & then
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
        verify { Random.nextLong(any(), any()) }
    }
}