package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class ScannerTest {
    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }
    @Test
    fun getScanData() {
        every { Random.nextLong(any(), any()) } returns 1L
        every { Random.nextBytes(any<Int>()) } returns ByteArray(1)
        val data = Scanner.getScanData()
        verify { Random.nextBytes(any<Int>()) }
        assertTrue(data.isNotEmpty())
    }
    @Test
    fun `throw ScanTimeoutException`() {
        assertThrows<ScanTimeoutException> {
            every { Random.nextLong(any(), any()) } returns 11_000L
            Scanner.getScanData()
        }
    }
}