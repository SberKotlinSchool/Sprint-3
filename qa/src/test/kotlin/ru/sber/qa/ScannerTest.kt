package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

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
    fun `should throw ScanTimeoutException if duration more than SCAN_TIMEOUT_THRESHOLD`() {
        every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun `should return data successfully with SCAN_TIMEOUT_THRESHOLD - 1`() {
        val expect = byteArrayOf()
        every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 1
        every { Random.nextBytes(any<Int>()) } returns expect

        val res = Scanner.getScanData()

        assertEquals(expect, res)
    }
}