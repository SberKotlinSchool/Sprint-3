package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

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

        mockkObject(HrDepartmentExchange)

        HrDepartment.receiveRequest(certificateRequest)
        //Нет ошибок - успех
        verify(exactly = 1) { HrDepartmentExchange.pushIncome(certificateRequest) }

        unmockkAll()
    }

    @Test
    fun `processNextRequest() вызов без ошибок`() {
        val certificateRequest = mockk<CertificateRequest>()
        val expectedCertificate = Certificate(certificateRequest, 1123, byteArrayOf())
        every { certificateRequest.process(any()) } returns expectedCertificate

        mockkObject(HrDepartmentExchange)
        every { HrDepartmentExchange.pollIncomeBox() } returns certificateRequest

        HrDepartment.processNextRequest(1123)

        verify(exactly = 1) { HrDepartmentExchange.pushOutcome(expectedCertificate) }
        unmockkAll()
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
}