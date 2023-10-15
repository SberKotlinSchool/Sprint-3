package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals

class ScannerTest {

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
        every { Random.nextLong(5000L, 15000L) } returns 0L
        every { Random.nextBytes(100) } returns ByteArray(100) { it.toByte() }

        val scanData = Scanner.getScanData()
        assertEquals(100, scanData.size)
    }

    @Test
    fun getScanDataScanTimeoutException() {
        every { Random.nextLong(5000L, 15000L) } returns 11000L

        assertThrows<ScanTimeoutException> {
            Scanner.getScanData()
        }
    }
}