package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

class ScannerTest {

    private val expectedData = ByteArray(100)

    @BeforeEach
    fun setUp() {
        mockkObject(Random)

        every { Random.nextBytes(100) } returns expectedData
    }

    @AfterEach
    fun closeUp() {
        unmockkAll()
    }

    @Test
    fun shouldThrowScanTimeoutException() {
        every {
            Random.nextLong(
                5000L,
                15000L
            )
        } returns setScannerTimeoutThreshold(Scanner.SCAN_TIMEOUT_THRESHOLD)

        assertThrows<ScanTimeoutException> {
            Scanner.getScanData()
        }
    }

    @Test
    fun shouldReturnScannerData() {
        every { Random.nextLong(5000L, 15000L) } returns setScannerTimeoutThreshold(-1000L)

        val newData = Scanner.getScanData()
        assertEquals(expectedData, newData)
    }

    private fun setScannerTimeoutThreshold(value: Long): Long {
        return Scanner.SCAN_TIMEOUT_THRESHOLD + value
    }
}
