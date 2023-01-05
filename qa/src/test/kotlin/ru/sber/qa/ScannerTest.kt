package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class ScannerTest {
    private lateinit var nextLong: MockKStubScope<Long, Long>
    @BeforeEach
    fun init() {
        mockkObject(Random)
        nextLong = every { Random.nextLong(any(), any()) }
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
    @Test
    fun `getScanData correct generate bytes`() {
        // Given
        nextLong returns 0L
        // When
        val res = Scanner.getScanData()
        // Then
        assertNotNull(res)
        assertTrue(res.size == 100)
    }

    @Test
    fun `getScanData throw timeout exception`() {
        // Given
        nextLong returns (Scanner.SCAN_TIMEOUT_THRESHOLD + 1)
        // When
        val res = runCatching { Scanner.getScanData() }
        // Then
        assertNotNull(res)
        assertThrows(ScanTimeoutException::class.java, { res.getOrThrow() }, "Таймаут сканирования документа")
    }

}