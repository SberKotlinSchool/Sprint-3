package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFailsWith


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
    fun `getScanData throws TimeoutScanTimeoutException`() {
        assertFailsWith(
            exceptionClass = ScanTimeoutException::class,
            block = {
                every { Random.nextLong(any(), any()) } returns 10_001L
                Scanner.getScanData()
            }
        )
    }

    @Test
    fun `getScanData returns ByteArray`() {
        every { Random.nextLong(5000L, 15000L) } returns 10000L
        val result = Scanner.getScanData()
        verify(exactly = 1) { Random.nextBytes(100) }
        assertEquals(result.size, 100)
    }
}