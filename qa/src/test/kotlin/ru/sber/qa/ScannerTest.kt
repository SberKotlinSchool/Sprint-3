package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScannerTest {

    @BeforeAll
    fun beforeAll() {
        unmockkAll()
        mockkObject(Random.Default)
    }

    @Test
    fun testGetScanDataSuccess() {
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 1
        Scanner.getScanData()
        verify { Random.nextBytes(100) }
    }

    @Test
    fun testGetScanDataFail() {
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1
        assertThrows(ScanTimeoutException::class.java) { Scanner.getScanData() }
    }
}