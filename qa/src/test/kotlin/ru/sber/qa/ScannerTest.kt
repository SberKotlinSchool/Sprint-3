package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random.Default)
    }

    @Test
    fun getScanData() {
        every { Random.nextLong(5000L, 15000L) } returns 1L

        val data = Scanner.getScanData()

        assertNotNull(data)
        assertTrue { data.size == 100 }
    }

    @Test
    fun `getScanData with ScanTimeoutException`() {
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        val exception = assertThrows<ScanTimeoutException> { Scanner.getScanData() }
        assertEquals("Таймаут сканирования документа", exception.message)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Random.Default)
    }

}