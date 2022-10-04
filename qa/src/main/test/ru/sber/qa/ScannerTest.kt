package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class ScannerTest {
    @BeforeEach
    fun setUp() {

        mockkObject(Random)
    }

    @Test
    fun testScanDataMinValueTimeoutException() {
        every { Random.nextLong(any(), any()) } returns 10001L
        assertThrows<ScanTimeoutException> {Scanner.getScanData()}
    }

    @Test
    fun testScanDataMaxValueTimeoutException() {
        every { Random.nextLong(any(), any()) } returns 15000L
        assertThrows<ScanTimeoutException> {Scanner.getScanData()}
    }

    @Test
    fun testScanDataMaxValueSupported() {
        every { Random.nextLong(any(), any()) } returns 10000L
        val scanData = Scanner.getScanData()
        assertNotNull(scanData)
        assertEquals(scanData.size, 100)
    }

    @Test
    fun testScanDataMinValueSupported() {
        every { Random.nextLong(any(), any()) } returns 5000L
        val scanData = Scanner.getScanData()
        assertNotNull(scanData)
        assertEquals(scanData.size, 100)
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}
