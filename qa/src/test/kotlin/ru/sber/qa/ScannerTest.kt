package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun init() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getScanData() успешно сканирует и возвращает значение`() {
        assertTrue(Scanner.SCAN_TIMEOUT_THRESHOLD > 0)
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 0

        val data = Scanner.getScanData()
        assertEquals(100, data.size)
    }

    @Test
    fun `getScanData() падает по таймауту при сканировании`() {
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        val exception = Assertions.assertThrows(ScanTimeoutException::class.java) { Scanner.getScanData() }
        assertEquals("Таймаут сканирования документа", exception.message)
    }
}