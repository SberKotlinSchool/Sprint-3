package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import org.junit.jupiter.api.Assertions.*
import kotlin.math.exp
import kotlin.random.Random

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
    fun `getScanData() timeout`() {
        // given
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1L

        // when then
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun `getScanData() success`() {
        // given
        val expected = "mockk".repeat(100/5).toByteArray()
        every { Random.nextBytes(100) } returns expected
        every { Random.nextLong(5000L, 15000L) } returns 5000L

        // when
        val result = Scanner.getScanData()

        // then
        assertEquals(result, expected)
        assertEquals(result.size, 100)

    }

}