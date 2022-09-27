package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import kotlin.random.Random

@DisplayName("Scanner class test cases")
internal class ScannerTest {

    @BeforeEach
    fun mockRandom() {
        mockkObject(Random)
    }

    @Test
    fun `getScanData getScanData() test throw ScanTimeoutException with mock`() {
        every { Random.nextLong(5000L, 15000L) } returns 11000L
        assertThrows(ScanTimeoutException::class.java) {
            Scanner.getScanData()
        }
    }

    @Test
    fun `getScanData getScanData() test not throw ScanTimeoutException with mock`() {
        val expectedDataSize = 100
        every { Random.nextLong(5000L, 15000L) } returns 9000L
        val resultScan = Scanner.getScanData()
        assertNotNull(resultScan, "result of getScanData is not null check")
        assertEquals(expectedDataSize, resultScan.size, "data size check")
    }

    @AfterEach
    fun unlockAllRandom() {
        unmockkAll()
    }
}

