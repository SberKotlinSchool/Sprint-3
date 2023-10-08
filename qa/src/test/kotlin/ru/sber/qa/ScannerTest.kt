package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import java.time.Duration.ofMillis
import kotlin.random.Random

class ScannerTest {

    @BeforeEach
    fun beforeTests() {
        mockkObject(Random)
    }

    @AfterEach
    fun afterTests() {
        unmockkObject(Scanner)
    }
    @Test
    @DisplayName("Ошибка при сканировани")
    fun scanDataExceptionsTest() {
        every { Random.nextLong(any(), any()) } returns 11_000L

        val exception = assertThrows(ScanTimeoutException::class.java) {
            Scanner.getScanData()
        }

       assertEquals("Таймаут сканирования документа", exception.message)
    }

    @Test
    @DisplayName("Проверка времени сканирования документа")
    fun scanDataTest() {
        every { Random.nextLong(any(), any()) } returns 5_000L

        assertTimeout(ofMillis(5_100L)) {
            Scanner.getScanData()
        }

    }
}