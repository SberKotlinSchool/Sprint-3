package test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.sber.qa.WeekendDayException

internal class WeekendDayExceptionTest{

    @Test
    fun testException() {
        val exception: Exception = assertThrows(WeekendDayException::class.java) {
            throw WeekendDayException()
        }
        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }
}
