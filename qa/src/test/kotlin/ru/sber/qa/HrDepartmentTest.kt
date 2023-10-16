package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import kotlin.test.assertEquals

internal class HrDepartmentTest {

    @Test
    fun `receiveRequest() выбрасывает WeekendDayException`() {

        val certificateRequest = mockk<CertificateRequest>()
        HrDepartment.clock = sundayClock()

        assertThrows(WeekendDayException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest() выбрасывает NotAllowReceiveRequestException для LABOUR_BOOK`() {
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        HrDepartment.clock = mondayClock()

        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest() выбрасывает NotAllowReceiveRequestException для NDFL`() {
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        HrDepartment.clock = tuesdayClock()

        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest() вызов без ошибок`() {
        val certificateRequest = mockk<CertificateRequest>()
        HrDepartment.clock = mondayClock()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        val expectedCertificate = Certificate(certificateRequest, 1123, byteArrayOf())
        every { certificateRequest.process(any()) } returns expectedCertificate

        val incomeBox = getPrivateFieldValue<LinkedList<CertificateRequest>>("incomeBox", HrDepartment.javaClass)
        assertEquals(0, incomeBox.size)

        //Вызов проверяемого метода
        HrDepartment.receiveRequest(certificateRequest)

        assertEquals(1, incomeBox.size)
        assertEquals(certificateRequest, incomeBox[0])
    }

    @Test
    fun `processNextRequest() вызов без ошибок`() {
        val certificateRequest = mockk<CertificateRequest>()
        val expectedCertificate = Certificate(certificateRequest, 1123, byteArrayOf())
        every { certificateRequest.process(any()) } returns expectedCertificate

        val incomeBox = getPrivateFieldValue<LinkedList<CertificateRequest>>("incomeBox", HrDepartment.javaClass)
        val outcomeOutcome = getPrivateFieldValue<LinkedList<Certificate>>("outcomeOutcome", HrDepartment.javaClass)
        assertEquals(0, outcomeOutcome.size)
        incomeBox.push(certificateRequest)
        assertEquals(1, incomeBox.size)

        //Вызов проверяемого метод
        HrDepartment.processNextRequest(1123)

        assertEquals(1, outcomeOutcome.size)
        assertEquals(expectedCertificate, outcomeOutcome[0])
        assertEquals(0, incomeBox.size)
    }


    //Понедельник
    private fun mondayClock() = Clock.fixed(
            LocalDateTime.of(1996, 1, 1, 0, 0).toInstant(ZoneOffset.UTC),
            ZoneId.from(ZoneOffset.UTC))

    //Вторник
    private fun tuesdayClock() = Clock.fixed(
            LocalDateTime.of(2008, 1, 1, 0, 0).toInstant(ZoneOffset.UTC),
            ZoneId.from(ZoneOffset.UTC))

    //Воскресенье
    private fun sundayClock() = Clock.fixed(
            LocalDateTime.of(2006, 1, 1, 0, 0).toInstant(ZoneOffset.UTC),
            ZoneId.from(ZoneOffset.UTC))


    /**
     * Получение приватных переменных ¯\_(ツ)_/¯
     */
    private fun <T> getPrivateFieldValue(fieldName: String, className: Class<HrDepartment>): T {
        val field = className.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(className) as T
    }
}