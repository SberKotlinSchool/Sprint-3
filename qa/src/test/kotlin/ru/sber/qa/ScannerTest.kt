package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.sber.qa.Scanner.SCAN_TIMEOUT_THRESHOLD
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @Test
    fun `getting ScanTimeoutException`() {
        every { Random.nextLong(5000L, 15000L) } returns SCAN_TIMEOUT_THRESHOLD + 1
        assertThrows<ScanTimeoutException> {
            Scanner.getScanData()
        }
    }

    @Test
    fun `getting some data`() {
        val someData = "someData".toByteArray()
        every { Random.nextLong(any(), any()) } returns 1L
        every { Random.nextBytes(100) } returns someData
        assertEquals(someData, Scanner.getScanData())
    }
}