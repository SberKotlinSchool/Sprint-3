package ru.sber.qa.component

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.sber.qa.getTestByteArray
import kotlin.random.Random
import kotlin.test.assertFailsWith

/**
 * Не получилось замокать Random внутри метода, только в Before
 * Поэтому тесты были разделены на два класса в зависимости от мока
 */
internal class ScannerTest {

    @BeforeEach
    fun init() {
        mockkObject(Random)
    }

    @Test
    @DisplayName("getScanData test - success")
    fun getScanData_success() {
        every { Random.nextLong(5000L, 15000L) } returns 6000L
        every { Random.nextBytes(100) } returns getTestByteArray()

        val actual = Scanner.getScanData()
        assertEquals(100, actual.size)
    }

    @Test
    @DisplayName("getScanData test - exception")
    fun getScanData_exception() {
        every { Random.nextLong(5000L, 15000L) } returns 12000L

        assertFailsWith<Exception> { Scanner.getScanData() }
    }
}