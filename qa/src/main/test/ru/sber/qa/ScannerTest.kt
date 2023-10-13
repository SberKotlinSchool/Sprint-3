package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    fun `getScanData should throw ScanTimeoutException if scan duration is more than SCAN_TIMEOUT_THRESHOLD`() {
        // given
        every { Random.nextLong(any(), any()) } returns SCAN_TIMEOUT_THRESHOLD + 1

        // then
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun `getScanData should return data successfully if scan duration is less than SCAN_TIMEOUT_THRESHOLD`() {
        // given
        val expected = byteArrayOf()
        every { Random.nextLong(any(), any()) } returns SCAN_TIMEOUT_THRESHOLD - 1
        every { Random.nextBytes(any<Int>()) } returns expected

        // when
        val result = Scanner.getScanData()

        // then
        assertEquals(expected, result)
    }

    companion object {
        private const val SCAN_TIMEOUT_THRESHOLD = 10_000L
    }
}