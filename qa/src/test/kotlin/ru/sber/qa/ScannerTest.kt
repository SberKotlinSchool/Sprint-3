package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals

internal class ScannerTest {
    @BeforeEach
    fun beforeTests() {
        mockkObject(Random)
    }

    @Test
    fun `getScanData() should throw ScanTimeoutException`() {
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1L

        assertThrows<ScanTimeoutException> {
            Scanner.getScanData()
        }
    }

    @Test
    fun `getScanData() should return ByteArray`() {
        val byteArray = ByteArray(100)

        every { Random.nextLong(5000L, 15000L) } returns 1L
        every { Random.nextBytes(100) } returns byteArray

        val actual = Scanner.getScanData()

        verify { Random.nextBytes(100) }
        assertNotNull(actual)
        assertEquals(expected = byteArray, actual = actual)
    }


    @AfterEach
    fun afterTests() {
        verify { Random.nextLong(5000L, 15000L) }
        unmockkAll()
    }
}