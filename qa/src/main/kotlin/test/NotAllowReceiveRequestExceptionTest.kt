package test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.sber.qa.NotAllowReceiveRequestException

internal class NotAllowReceiveRequestExceptionTest{

    @Test
    fun testException() {
        val exception: Exception = assertThrows(NotAllowReceiveRequestException::class.java) {
            throw NotAllowReceiveRequestException()
        }
        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

}
