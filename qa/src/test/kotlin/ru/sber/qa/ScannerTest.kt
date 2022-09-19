package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ScannerTest {
    @Test
    fun getScanDataReturnResult() {
        val expectedResult = "Byte array".toByteArray()

        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 10_000L
        every { Random.nextBytes(100) }.returns(expectedResult)

        assertEquals(expectedResult, Scanner.getScanData())
    }

    @Test
    fun getScanDataWhenException() {
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 10_001L

        val exception = kotlin.runCatching {
            Scanner.getScanData()
        }.exceptionOrNull()

        assertNotNull(exception)
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}