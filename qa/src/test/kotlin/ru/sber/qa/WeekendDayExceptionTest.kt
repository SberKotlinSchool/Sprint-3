package ru.sber.qa

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

private const val MESSAGE = "Заказ справков в выходной день не работает"

internal class WeekendDayExceptionTest {

    private val sut = WeekendDayException()

    @Test
    fun shouldHaveEqualsMessages() {
        assertEquals(MESSAGE, sut.message)
    }
}