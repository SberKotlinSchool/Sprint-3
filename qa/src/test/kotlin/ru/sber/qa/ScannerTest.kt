package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals

internal class ScannerTest {

    @BeforeEach
    fun setup() {
        mockkObject(Random)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun getScanDataSuccess() {
        // Arrange
        val expectedResult = "SimpleText".toByteArray()

        every {
            Random.nextLong(any(), any())
        } returns 0

        every {
            Random.nextBytes(size = any())
        } returns expectedResult

        // Act
        val result = Scanner.getScanData()

        // Assert
        assertEquals(result, expectedResult)
    }

    @Test
    fun getScanDataException() {
        // Arrange
        every {
            Random.nextLong(any(), any())
        } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        // Act & Assert
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }
}