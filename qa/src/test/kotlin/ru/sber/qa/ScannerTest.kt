package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals

internal class ScannerTest {


    @Test
    fun scanTimeoutTest() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 15_000L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun getScanDataTest() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 100L
        val array = "123".toByteArray()
        every { Random.nextBytes(100) } returns array
        assertEquals(array, Scanner.getScanData())
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}