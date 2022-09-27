package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertTrue

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
    fun getScanDataWithoutExceptions() {
        every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 1L

        var resultData = Scanner.getScanData()

        verify { Random.nextLong(any(), any()) }
        assertTrue { resultData.size == 100 }
        assertTrue { resultData is ByteArray }
    }

    @Test
    fun getScanDataWithExceptions() {
        every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }
}