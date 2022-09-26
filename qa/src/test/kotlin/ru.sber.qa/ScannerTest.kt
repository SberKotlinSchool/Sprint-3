package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith

internal class ScannerTest {


    @Test
    fun `getScanData with ScanTimeoutException`() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 10_001L
        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun `getScanData`() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 10000L
        val randomArray = Random.nextBytes(100)
        every { Random.nextBytes(100) } returns randomArray
        assertEquals(randomArray, Scanner.getScanData())
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}
