package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals

class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random.Default)
    }

    @Test
    fun testGetScanData() {
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }

        every { Random.nextLong(5000L, 15000L) } returns 1L
        assertEquals(Scanner.getScanData().size, 100)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Random.Default)
    }

}