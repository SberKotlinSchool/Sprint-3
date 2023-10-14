package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class ScannerTest {

    private val data = ByteArray(100)

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun shouldGetScanDataWithoutTimeout() {
        every { Random.nextLong(5000L, 15000L) } returns 9999L
        every { Random.nextBytes(100) } returns data
        var scanData: ByteArray? = null
        assertDoesNotThrow {
            scanData = Scanner.getScanData()
        }
        assertEquals(data, scanData)
    }

    @Test
    fun shouldGetScanDataWithTimeout() {
        every { Random.nextLong(5000L, 15000L) } returns 10001L
        assertThrows<ScanTimeoutException> {
            Scanner.getScanData()
        }
    }
}