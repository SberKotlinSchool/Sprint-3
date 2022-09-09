package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

@DisplayName("Scanner class test cases")
internal class ScannerTest {

    private val expectedDataSize = 100


    @Test
    fun `Scanner getScanData() test without mocks`() {

        try {

            val resultScan = Scanner.getScanData()
            assertNotNull(resultScan, "result of getScanData is not null check")
            assertEquals(expectedDataSize, resultScan.size, "data size check")

        } catch (e: ScanTimeoutException) {
            assertEquals(e.message, "Таймаут сканирования документа", "Exception check")
        }
    }

    @Test
    fun `getScanData getScanData() test throw ScanTimeoutException with mock`() {

        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 11000L

        assertThrows(ScanTimeoutException::class.java) {
            Scanner.getScanData()
        }
    }

    @Test
    fun `getScanData getScanData() test not throw ScanTimeoutException with mock`() {

        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 9000L
        val resultScan = Scanner.getScanData()
        assertNotNull(resultScan, "result of getScanData is not null check")
        assertEquals(expectedDataSize, resultScan.size, "data size check")
    }

}