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
    fun getScanDataTest() {

        every { Random.nextLong(5000L, 15000L) } returnsMany listOf(14999L, 5000L)

        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }

        val scanData = Scanner.getScanData()

        assertEquals(100, scanData.size)

        verify (exactly = 2) {
            Random.nextLong(5000L, 15000L)
        }
    }
}