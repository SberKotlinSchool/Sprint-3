package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith

class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @Test
    fun testGetScanDataException() {
        every { Random.nextLong(5000L, 15000L) } returns 10001L
        assertFailsWith(
            ScanTimeoutException::class
        ) { Scanner.getScanData() }
    }

    @Test
    fun testGetScanDataSuccess() {
        every { Random.nextLong(5000L, 15000L) } returns 900L
        Scanner.getScanData()
        verify(exactly = 1) { Random.nextBytes(100) }
    }

}