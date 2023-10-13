package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.lang.reflect.Field
import java.time.*
import java.util.*
import kotlin.random.Random


class HrDepartmentTest {
    private val certificateRequest = mockk<CertificateRequest>()
    private lateinit var certificate: Certificate
    @BeforeEach
    fun beforeTests() {
        certificate = Certificate(certificateRequest, Random.nextLong(), Random.nextBytes(100))
        every { certificateRequest.process(any()) } returns certificate
    }

    @AfterEach
    fun afterTests() {
        unmockkObject(certificateRequest)
    }

    @Test
    @DisplayName("Заказ справки в выходной день")
    fun receiveRequestExceptionsTest() {
        val dateTime = LocalDateTime.of(2023, Month.OCTOBER, 8, 10, 0, 0)
        HrDepartment.clock =  Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

        val exception = assertThrows(WeekendDayException::class.java) {
            HrDepartment.receiveRequest(certificateRequest)
        }

        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }

    @Test
    @DisplayName("Нет разрешения на принятие запроса")
    fun receiveRequestNoPermissionExceptionsTest() {
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        val dateTime = LocalDateTime.of(2023, Month.OCTOBER, 9, 10, 0, 0)
        HrDepartment.clock =  Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)


        val exception = assertThrows(NotAllowReceiveRequestException::class.java) {
            HrDepartment.receiveRequest(certificateRequest)
        }

        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

    @Test
    @DisplayName("Успешное получение запроса")
    fun receiveRequestSuccessTest() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        val dateTime = LocalDateTime.of(2023, Month.OCTOBER, 9, 10, 0, 0)
        HrDepartment.clock =  Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

        HrDepartment.receiveRequest(certificateRequest)

        val incomeBox = getPrivateFieldsHrDepartment("incomeBox")

        assertEquals(certificateRequest, incomeBox.first)
    }

    @Test
    @DisplayName("Обработка запросов")
    fun processNextRequestTest() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        val dateTime = LocalDateTime.of(2023, Month.OCTOBER, 9, 10, 0, 0)
        HrDepartment.clock =  Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)
        val incomeBox = getPrivateFieldsHrDepartment("incomeBox") as LinkedList<CertificateRequest>
        incomeBox.push(certificateRequest)

        HrDepartment.processNextRequest(Random.nextLong())

        val outcomeOutcome = getPrivateFieldsHrDepartment("outcomeOutcome")

        assertEquals(certificate, outcomeOutcome.first)
        verify { certificateRequest.process(any()) }
    }


    private fun getPrivateFieldsHrDepartment(nameField: String): LinkedList<*> {
        val field: Field = HrDepartment::class.java.getDeclaredField(nameField)
        field.setAccessible(true)

        return field.get(HrDepartment) as LinkedList<*>
    }
}