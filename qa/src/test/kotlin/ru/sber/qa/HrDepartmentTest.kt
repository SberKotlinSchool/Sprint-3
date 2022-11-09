package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.util.*
import kotlin.test.assertEquals


internal class HrDepartmentTest {

    private val weekendlist: MutableList<Clock> = mutableListOf()
    private val alldayList: MutableList<Clock> = mutableListOf()

    //Список верных вариантов

    //Список вариантов переменной clock
    fun clock_value(x: Int): Clock {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
        calendar.set(Calendar.DAY_OF_WEEK, x)
        return Clock.fixed(Instant.parse(formatter.format(calendar.time)), ZoneId.of("Europe/Moscow"))
    }


    //Передаем входящие данные в виде двух списков
    @BeforeEach
    fun startValue() {
        //Список выходных
        for (f in 1..7) {
            if (f == 1 || f == 7) {
                weekendlist.add(clock_value(f))
            } else {
                alldayList.add(clock_value(f))
            }
        }
    }


    //Очищаем список mockk
    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //--------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------
    // Тест функции receiveRequest


    //--------------------------------------------------------------------------------------
    //Проверяем на возможную ошибку в части условий - WeekendDayException (2 дня отдельно)
    @Test
    fun WeekendDayException_Test() {
        for (f in weekendlist) {
            val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную
            every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
            HrDepartment.clock = f // Меняем var-день недели
            assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
        }

    }

    //--------------------------------------------------------------------------------------
    //Проверяем на возможную ошибку в части условий - NotAllowReceiveRequestException (5 дней отдельно)
    @Test
    fun NotAllowReceiveRequestException_Test() {
        for (f in alldayList) {
            val dayOfWeek_need = LocalDateTime.now(f).dayOfWeek
            if (dayOfWeek_need in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)) {
                val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную LABOUR_BOOK
                every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной
                HrDepartment.clock = f // Меняем var-день недели
                assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
            }
            if (dayOfWeek_need in listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)) {
                val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную NDFL
                every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
                HrDepartment.clock = f // Меняем var-день недели
                assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) } //Проверяем конкретную ошибку
            }
        }
    }

    //--------------------------------------------------------------------------------------
    //Проверяем на другие ошибки

    @Test
    fun Done_Test() {
        for (f in alldayList) {
            val dayOfWeek_need = LocalDateTime.now(f).dayOfWeek
            if (dayOfWeek_need in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)) {
                val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную NDFL
                every { certificateRequest.certificateType } returns CertificateType.NDFL // Присваиваем тип переменной
                HrDepartment.clock = f
                assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }  // Проверяем другие ошибки
            }
            if (dayOfWeek_need in listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)) {
                val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную LABOUR_BOOK
                every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK // Присваиваем тип переменной
                HrDepartment.clock = f
                assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }  // Проверяем другие ошибки
            }
        }
    }


    //--------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------
    // Тест функции processNextRequest

    @Test
    fun NextRequest_Test() {
        val processNextRequest: Long = (1..123L).random() //Рандомное число
        val certificateRequest: CertificateRequest = mockk() // Формируем пустышку-переменную
        val needCertificate = mockk<Certificate>()
        every { certificateRequest.process(processNextRequest) } returns needCertificate //Присваиваем под текущую цифру процесс
        val incomeBox: LinkedList<CertificateRequest> = LinkedList() //Копия переменной incomeBox из HrDepartment
        val outcomeOutcome: LinkedList<Certificate> = LinkedList() //Копия переменной outcomeOutcome из HrDepartment
        val incomeBoxCopy: LinkedList<CertificateRequest> //Вариант для рефлексии (входящий)
        HrDepartment.javaClass.getDeclaredField("incomeBox").let {
            it.isAccessible = true
            incomeBoxCopy = it.get(incomeBox) as LinkedList<CertificateRequest>
        } //Сама рефлексия (входящий)

        incomeBoxCopy.push(certificateRequest)

        val outcomeOutcomeCopy: LinkedList<Certificate> //Вариант для рефлексии (исходящий)
        HrDepartment.javaClass.getDeclaredField("outcomeOutcome").let {
            it.isAccessible = true
            outcomeOutcomeCopy = it.get(outcomeOutcome) as LinkedList<Certificate>
        } //Сама рефлексия (исходящий)

        assertDoesNotThrow { HrDepartment.processNextRequest(processNextRequest) }  // Проверяем другие ошибки
        assertEquals(needCertificate, outcomeOutcomeCopy.first) //Проверка результата
    }
}


