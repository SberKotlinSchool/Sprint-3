package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random
import kotlin.test.assertFailsWith


class ScannerTest {

    @Test
    fun GetScanDataTest() {
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 100L
        val byteArray = Scanner.getScanData()
        assertEquals(100, byteArray.size)
        unmockkAll()
    }

    @Test
    fun exceptionTest() {
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 11000L
        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }
        unmockkAll()
    }
}
