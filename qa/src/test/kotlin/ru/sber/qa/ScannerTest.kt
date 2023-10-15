package ru.sber.qa

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith

class ScannerTest {
    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should getScanData return data size = 100`() {
        //given
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 100

        //when
        val data = Scanner.getScanData()

        //then
        assertEquals(100, data.size)
    }

    @Test
    fun `should getScanData throw ScanTimeoutException`() {
        //given
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 100

        //when
        //then
        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }
    }
}