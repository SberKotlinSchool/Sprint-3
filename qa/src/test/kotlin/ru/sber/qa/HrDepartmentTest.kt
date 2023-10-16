package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*

internal class HrDepartmentTest {

    @BeforeEach
    fun setUp() {
        mockkObject(HrDepartment)
        mockkObject(Scanner)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test receiveRequest with NDFL on a valid day`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-09T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.NDFL)

        HrDepartment.receiveRequest(certificateRequest)

        val incomeBox = getPrivateFieldIncomeBoxValue("incomeBox")
        assertTrue(incomeBox.contains(certificateRequest))
    }

    @Test
    fun `test receiveRequest with Labour Book on a valid day`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-12T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.LABOUR_BOOK)

        HrDepartment.receiveRequest(certificateRequest)

        val incomeBox = getPrivateFieldIncomeBoxValue("incomeBox")
        assertTrue(incomeBox.contains(certificateRequest))
    }

    @Test
    fun `test receiveRequest on a weekend day`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-15T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.NDFL)

        val exception = assertThrows(WeekendDayException::class.java) {
            HrDepartment.receiveRequest(certificateRequest)
        }
        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }

    @Test
    fun `test receiveRequest on a non-allowed day for NDFL`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-12T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.NDFL)

        val exception = assertThrows(NotAllowReceiveRequestException::class.java) {
            HrDepartment.receiveRequest(certificateRequest)
        }
        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

    @Test
    fun `processNextRequest should process a certificate request`() {
        // Создаем мок-объекты CertificateRequest и Certificate
        val certificateRequest = mockk<CertificateRequest>()
        val certificate = mockk<Certificate>()
        // Заполняем incomeBox мок-запросом
        getPrivateFieldIncomeBoxValue("incomeBox").add(certificateRequest)
        // Устанавливаем поведение мок-объектов
        every { certificateRequest.process(any()) } returns certificate
        // Вызываем метод, который тестируем
        HrDepartment.processNextRequest(123L)
        // Проверяем, что outcomeOutcome содержит ожидаемый Certificate
        assertEquals(certificate, getPrivateFieldOutcomeValue("outcomeOutcome").first())
    }

    @Test
    fun `test processNextRequest when incomeBox is not empty`() {
        // Создаем мок-объекты
        val certificateRequest = mockk<CertificateRequest>()
        val certificate = mockk<Certificate>()
        // Устанавливаем значение incomeBox
        getPrivateFieldIncomeBoxValue("incomeBox").add(certificateRequest)
        val outcome = getPrivateFieldOutcomeValue("outcomeOutcome")
        // Мокируем вызовы методов
        every { certificateRequest.process(any()) } returns certificate
        // Вызываем метод processNextRequest
        HrDepartment.processNextRequest(123L)
        // Проверяем, что outcomeOutcome обновился
        assertEquals(1, outcome.size)
    }

    private fun getPrivateFieldIncomeBoxValue(fieldName: String): LinkedList<CertificateRequest>  {
        val field = HrDepartment::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(HrDepartment) as LinkedList<CertificateRequest>
    }

    private fun getPrivateFieldOutcomeValue(fieldName: String): LinkedList<Certificate>  {
        val field = HrDepartment::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(HrDepartment) as LinkedList<Certificate>
    }
}