package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.util.*



internal class HrDepartmentTest {

    //Список верных вариантов


    //Список вариантов переменной clock
    fun clock_value(x: Int): Clock {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
        calendar.set(Calendar.DAY_OF_WEEK, x)
        return Clock.fixed(Instant.parse(formatter.format(calendar.time)), ZoneId.of("Europe/Moscow"))
    }

    //Очищаем список mockk

    @AfterEach
    fun tearDown() {unmockkAll()}

    //--------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------
    // Тест функции receiveRequest



    //--------------------------------------------------------------------------------------
    //Проверяем на возможную ошибку в части условий - WeekendDayException (2 дня отдельно)

    @Test
    fun SUNDAY_WeekendDayException_Test() {
        val clock = clock_value(1) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную
        every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
        HrDepartment.clock = clock // Меняем var-день недели
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
    }


    @Test
    fun SATURDAY_WeekendDayException_Test() {
        val clock = clock_value(7) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную
        every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
        HrDepartment.clock = clock // Меняем var-день недели
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
        }

    //--------------------------------------------------------------------------------------
    //Проверяем на возможную ошибку в части условий - NotAllowReceiveRequestException (5 дней отдельно)

    @Test
    fun MONDAY_NotAllowReceiveRequestException_Test() {
        val clock = clock_value(2) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную LABOUR_BOOK
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной
        HrDepartment.clock = clock // Меняем var-день недели
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
    }
    @Test
    fun WEDNESDAY_NotAllowReceiveRequestException_Test() {
        val clock = clock_value(4) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную LABOUR_BOOK
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной
        HrDepartment.clock = clock // Меняем var-день недели
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
    }
    @Test
    fun FRIDAY_NotAllowReceiveRequestException_Test() {
        val clock = clock_value(6) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную LABOUR_BOOK
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной
        HrDepartment.clock = clock // Меняем var-день недели
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
    }

    @Test
    fun TUESDAY_NotAllowReceiveRequestException_Test() {
        val clock = clock_value(3) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную NDFL
        every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
        HrDepartment.clock = clock // Меняем var-день недели
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
    }

    @Test
    fun THURSDAY_NotAllowReceiveRequestException_Test() {
        val clock = clock_value(5) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную NDFL
        every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
        HrDepartment.clock = clock // Меняем var-день недели
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
    }

    //--------------------------------------------------------------------------------------
    //Проверяем на другие ошибки
    @Test
    fun MONDAY_Done_Test() {
        val clock = clock_value(2) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную NDFL
        every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
        HrDepartment.clock = clock
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }  // Проверяем другие ошибки
    }

    @Test
    fun WEDNESDAY_Done_Test() {
        val clock = clock_value(4) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную NDFL
        every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
        HrDepartment.clock = clock
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }  // Проверяем другие ошибки
    }

    @Test
    fun FRIDAY_Done_Test() {
        val clock = clock_value(6) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную NDFL
        every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
        HrDepartment.clock = clock
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }  // Проверяем другие ошибки
    }

    @Test
    fun TUESDAY_Done_Test() {
        val clock = clock_value(3) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную LABOUR_BOOK
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной
        HrDepartment.clock = clock
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }  // Проверяем другие ошибки
    }

    @Test
    fun THURSDAY_Done_Test() {
        val clock = clock_value(5) // Определяем день недели
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную LABOUR_BOOK
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной
        HrDepartment.clock = clock
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }  // Проверяем другие ошибки
    }

    //--------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------
    // Тест функции processNextRequest


    //--------------------------------------------------------------------------------------
    //Проверяем на ошибки NDFL
    @Test
    fun NextRequest_NDFL_Test() {
        var counts: Int = 0
        //Проверяем 15 рандомных цифр
        while (counts < 15) {
            val processNextRequest: Long = (1..123L).random() //Рандомное число
            val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную
            HrDepartment.clock = clock_value(6) //FRIDAY
            every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной NDFL
            every { certificateRequest.process(processNextRequest) } returns mockk() //Присваиваем под текущую цифру процесс
            HrDepartment.receiveRequest(certificateRequest)
            assertDoesNotThrow { HrDepartment.processNextRequest(processNextRequest) }  // Проверяем другие ошибки
            counts += 1
        }
    }
    //--------------------------------------------------------------------------------------
    //Проверяем на ошибки LABOUR_BOOK
    @Test
    fun NextRequest_LABOUR_BOOK_Test() {
        var counts: Int = 0
        //Проверяем 15 рандомных цифр
        while (counts < 15){
            val processNextRequest: Long = (1..123L).random() //Рандомное число
            val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную
            HrDepartment.clock = clock_value(3) // TUESDAY
            every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной LABOUR_BOOK
            every { certificateRequest.process(processNextRequest) } returns mockk()
            HrDepartment.receiveRequest(certificateRequest)
            assertDoesNotThrow { HrDepartment.processNextRequest(processNextRequest) }  // Проверяем другие ошибки
            counts +=1
        }
    }

}




    /*
    fun clock_value(): MutableList<Clock> {
        val calendar = Calendar.getInstance()
        val listClock: MutableList<Clock> = mutableListOf()
        for (f in 0..6) {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
            if (f != 0){calendar.add(Calendar.DATE, 1)}
            var dates: Clock = Clock.fixed(Instant.parse(formatter.format(calendar.time)), ZoneId.of("Europe/Moscow"))
            listClock.add(dates)
        }
        return listClock
    }
    @Test
    fun Test_receiveRequest() {
        val certificateNDFL: CertificateRequest = mockk()
        val certificateLABOUR_BOOK: CertificateRequest = mockk()
        val clock = clock_value()
        val HrDepartment_copy: HrDepartment
        HrDepartment_copy = spyk()
        every { certificateNDFL.certificateType } returns CertificateType.NDFL
        every { certificateLABOUR_BOOK.certificateType } returns CertificateType.LABOUR_BOOK
        for (days_week in clock) {
                val certificateRequest = certificateLABOUR_BOOK
                HrDepartment.clock = days_week
                assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
            }
        }

    }

     */


