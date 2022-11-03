package test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.sber.qa.ScanTimeoutException

internal class ScanTimeoutExceptionTest{

    @Test
    fun testException() {
        val exception: Exception = assertThrows(ScanTimeoutException::class.java) {
            throw ScanTimeoutException()
        }
        assertEquals("Таймаут сканирования документа", exception.message)
    }
}