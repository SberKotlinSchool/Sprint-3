package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ScannerTest {

    @Test
    fun getScanDataNegative() {
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 16000L

        assertFailsWith<ScanTimeoutException> {  Scanner.getScanData()  }
    }

    @Test
    fun getScanDataPositive() {
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 5000L

        val result =  Scanner.getScanData()
        assertEquals(result.size, 100)
    }
}