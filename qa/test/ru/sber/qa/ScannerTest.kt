package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import kotlin.random.Random
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ScannerTest {
    private val service = Scanner

    @Test
    fun `getScanData should throw ScanTimeoutException if scan duration more than SCAN_TIMEOUT_THRESHOLD`() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 10_001L
        assertThrows<ScanTimeoutException> { service.getScanData() }
    }

    @Test
    fun `getScanData should return byte array`() {
        mockkObject(Random)
        val expectedResult = byteArrayOf(10)
        every { Random.nextLong(5000L, 15000L) } returns 9999L
        every { Random.nextBytes(100) } returns expectedResult
        assertEquals(expectedResult, service.getScanData())
    }
}