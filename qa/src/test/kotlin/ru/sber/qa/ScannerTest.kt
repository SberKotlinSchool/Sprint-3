package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.sber.qa.Scanner.SCAN_TIMEOUT_THRESHOLD
import kotlin.random.Random

internal class ScannerTest {

    val bytes = ByteArray(10)

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
        every { Random.nextBytes(any<Int>()) } returns bytes
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getScanDataNormalTest() {
        every { Random.nextLong(any(), any()) } returns 100L

        val result = assertDoesNotThrow(Scanner::getScanData)
        assertArrayEquals(bytes, result)
    }

    @Test
    fun getScanDataExceptionTest() {
        every { Random.nextLong(any(), any()) } returns SCAN_TIMEOUT_THRESHOLD + 100L

        assertThrows(ScanTimeoutException::class.java) { Scanner.getScanData() }
    }
}