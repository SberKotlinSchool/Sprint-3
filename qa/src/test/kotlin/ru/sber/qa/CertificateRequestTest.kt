package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random



internal class CertificateRequestTest {

    //Очищаем список mockk
    @AfterEach
    fun tearDown() {unmockkAll()}


    //Тест функции
    @Test
    fun processTest() {
        val bestRequest = CertificateRequest(employeeNumber = 3000L, certificateType = CertificateType.NDFL) //Формируем исходный шаблон
        mockkObject(Scanner) // Формируем пустышку по Object
        val ByteArray_test = Random.nextBytes(40) //Случайный ByteArray
        every { Scanner.getScanData() } returns ByteArray_test //Задаём результат функции
        var counts: Int = 0



        val hremployeenumber: Long = (1..3500L).random() //Случайная цифра
        val now_result = bestRequest.process(hrEmployeeNumber = hremployeenumber) //Задаём результат функции
        assertEquals(now_result.certificateRequest, bestRequest) //Проверка переменной certificateRequest
        assertEquals(now_result.processedBy, hremployeenumber) //Проверка переменной processedBy
        assertEquals(now_result.data, ByteArray_test) //Проверка переменной data

        verify () { Scanner.getScanData() } //Проверка, что было обращение к функции внутри тестируемого кода
    }
}