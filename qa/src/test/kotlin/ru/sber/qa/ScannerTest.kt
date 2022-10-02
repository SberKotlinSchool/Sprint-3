package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun before() {
        mockkObject(Random)
    }

    @Test
    fun getScanDataTestWithTimeout() {
        every { Random.nextLong(any(), any()) } returns 10001
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun getScanDataTest() {
        every { Random.nextLong(any(), any()) } returns 1
        val byteArray = ByteArray(0)
        every { Random.nextBytes(any<Int>()) } returns byteArray
        assertEquals(byteArray, Scanner.getScanData())
    }
}