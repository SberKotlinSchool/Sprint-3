package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ScannerTest {

    @BeforeEach
    fun init() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Random)
    }

    @Test
    fun getScanDataExceptionTest() {

        every { Random.nextLong(5000L, 15000L) } returns 14999L

        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }

        verify {
            Random.nextLong(5000L, 15000L)
        }
    }

    @Test
    fun getScanDataSuccessTest() {

        every { Random.nextLong(5000L, 15000L) } returns 5000L

        val scanData = Scanner.getScanData()

        assertEquals(100, scanData.size)

        verify {
            Random.nextLong(5000L, 15000L)
        }
    }

}