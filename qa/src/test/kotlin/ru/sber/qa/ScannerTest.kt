package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getScanData should return ByteArray`()
    {
        val size = 100
        every { Random.nextLong(any(), any()) } returns 100L

        val result = Scanner.getScanData()

        verify { Random.nextBytes(size) }

        assertNotNull(result)
        assertEquals(result.size, size)
    }

    @Test()
    fun `getScanData should throw ScanTimeoutException`() {
        assertThrows<ScanTimeoutException>
        {
            every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD * 2;
            Scanner.getScanData();
        }
    }
}