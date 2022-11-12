package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.assertThrows
import kotlin.random.Random


internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getScanDataNoTimeout() {
        every { Random.nextLong (any(),any()) } returns 5000L

        var scan = Scanner.getScanData()
        assertTrue { scan.size == 100 } // размер скана 100
        assertTrue { scan is ByteArray } // тип ByteArray
    }

    @Test
    fun getScanDataTimeoutScanTimeoutException() {
        every { Random.nextLong (any(),any()) } returns 10_001L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() } // ловим exception
    }
}