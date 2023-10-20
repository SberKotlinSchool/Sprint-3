package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith

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
    fun testGetScanDataSuccess() {
        every { Random.nextLong(any(), any()) } returns 100L
        val byteArray = Scanner.getScanData()
        assertEquals(100, byteArray.size)
    }

    @Test
    fun testGetScanDataTimeoutException() {
        every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1
        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }
    }
}