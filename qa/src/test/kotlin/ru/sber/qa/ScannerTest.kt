package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.random.Random
import kotlin.test.assertEquals

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
    fun throwScanTimeoutException() {
        //given
        val timeout = 11_000L
        val expectedMessage = "Таймаут сканирования документа"
        every { Random.nextLong(5000L, 15000L) } returns timeout

        //then
        val exception = assertThrows(ScanTimeoutException::class.java) { Scanner.getScanData() }
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun getScanData() {
        //given
        val duration = 6_000L
        val expectedResult = ByteArray(100)
        every { Random.nextLong(5000L, 15000L) } returns duration
        every { Random.nextBytes(100) } returns expectedResult

        //then
        val actualResult = assertDoesNotThrow { Scanner.getScanData() }
        assertEquals(expectedResult, actualResult)
    }
}