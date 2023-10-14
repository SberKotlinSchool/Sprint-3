package ru.sber.qa

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

private const val MESSAGE = "Таймаут сканирования документа"

internal class ScanTimeoutExceptionTest {

    private val sut = ScanTimeoutException()

    @Test
    fun shouldHaveEqualsMessages() {
        assertEquals(MESSAGE, sut.message)
    }
}