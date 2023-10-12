package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class ScannerTest{


    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }
    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
    @Test
    fun getScanError() {
        every { Random.nextLong(any(), any()) } returns 10_001L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
        verify(exactly = 1) { Random.nextLong(any(), any()) }
    }
    @Test
    fun getScanSuccess() {
        every { Random.nextLong (any(),any()) } returns 10_000L
        val scanData = Scanner.getScanData()
        verify(exactly = 1) { Random.nextLong(any(), any()) }
        assertTrue{ 100 == scanData.size }
        assertTrue{ scanData is ByteArray  }
    }

}