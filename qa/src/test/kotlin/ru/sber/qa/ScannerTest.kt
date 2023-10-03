package ru.sber.qa

import io.mockk.*
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ScannerTest {

    @Test
    fun getScanDataSuccessTest() {
        mockkObject(Random)

        val byteArray = ByteArray(22)

        every { Random.nextLong(5000L, 15000L) } returns 100L
        every { Random.nextBytes(100) } returns byteArray

        assertEquals(byteArray, Scanner.getScanData())

        unmockkObject(Random)
    }

    @Test
    fun getScanDataTimeoutTest() {
        mockkObject(Random)

        every { Random.nextLong(5000L, 15000L) } returns 15000L

        assertFailsWith<ScanTimeoutException>(block = { Scanner.getScanData() })

        unmockkObject(Random)
    }
}