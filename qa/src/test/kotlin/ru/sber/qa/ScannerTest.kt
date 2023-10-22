package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.sber.qa.Scanner.SCAN_TIMEOUT_THRESHOLD
import kotlin.random.Random

class ScannerTest {

    @BeforeEach
    fun beforeTest() {
        mockkObject(Random)
    }

    @AfterEach
    fun afterTest() {
        unmockkObject(Random)
    }

    @Test
    fun getScanData() {
        every { Random.nextLong(any(), any()) } returns (SCAN_TIMEOUT_THRESHOLD)
        val byteArray = byteArrayOf(1)
        every { Random.nextBytes(any<Int>()) } returns byteArray

        val scanData = Scanner.getScanData()

        verify(exactly = 1) { Random.nextLong(5000L, 15000L) }
        verify(exactly = 1) { Random.nextBytes(100) }
        assertEquals(byteArray, scanData)
    }

    @Test
    fun getScanDataThrowsScanTimeoutException() {
        every { Random.nextLong(any(), any()) } returns (SCAN_TIMEOUT_THRESHOLD + 1)

        assertThrows<ScanTimeoutException> { Scanner.getScanData() }

        verify(exactly = 1) { Random.nextLong(any(), any()) }
    }
}