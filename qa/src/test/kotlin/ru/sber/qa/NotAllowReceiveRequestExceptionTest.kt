package ru.sber.qa

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

private const val MESSAGE = "Не разрешено принять запрос на справку"

internal class NotAllowReceiveRequestExceptionTest {

    private val sut = NotAllowReceiveRequestException()

    @Test
    fun shouldHaveEqualsMessages() {
        assertEquals(MESSAGE, sut.message)
    }
}