package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

internal class ScannerTest {

    @BeforeEach
    internal fun setUp() {
        mockkObject(Random.Default)
    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getScanData with ScanTimeoutException`() {

        every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun getScanData() {
        every { Random.nextLong(any(), any()) } returns 1L
        val scanData = Scanner.getScanData()

        assertNotNull(scanData)
        verify { Random.nextBytes(100) }
    }
}