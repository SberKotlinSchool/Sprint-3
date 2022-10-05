package ru.sber.qa.test

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import ru.sber.qa.*
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.LinkedList

internal class HrDepartmentTest {

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestWithWeekendDayExceptionSaturday() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.SATURDAY, CertificateType.NDFL)

        // when & then
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWithWeekendDayExceptionSunday() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.SUNDAY, CertificateType.NDFL)

        // when & then
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWithNotAllowReceiveRequestExceptionMonday() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.MONDAY, CertificateType.LABOUR_BOOK)

        // when & then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWithNotAllowReceiveRequestExceptionTuesday() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.TUESDAY, CertificateType.NDFL)

        // when & then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWithNotAllowReceiveRequestExceptionWednesday() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.WEDNESDAY, CertificateType.LABOUR_BOOK)

        // when & then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWithNotAllowReceiveRequestExceptionThursday() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.THURSDAY, CertificateType.NDFL)

        // when & then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWithNotAllowReceiveRequestExceptionFriday() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.FRIDAY, CertificateType.LABOUR_BOOK)

        // when & then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestMondayNDFL() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.MONDAY, CertificateType.NDFL)

        // when & then
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTuesdayLabour() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.TUESDAY, CertificateType.LABOUR_BOOK)

        // when & then
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWednesdayNDFL() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.WEDNESDAY, CertificateType.NDFL)

        // when & then
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestThursdayLabour() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.THURSDAY, CertificateType.LABOUR_BOOK)

        // when & then
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestFridayNDFL() {
        // given
        val certificateRequest = receiveRequest(DayOfWeek.FRIDAY, CertificateType.NDFL)

        // when & then
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequestFridayNDFL() {
        // given
        val hrEmployeeNumber = 100L
        val certificateRequest = receiveRequest(DayOfWeek.FRIDAY, CertificateType.NDFL)
        every { certificateRequest.process(hrEmployeeNumber) } returns mockk()
        HrDepartment.receiveRequest(certificateRequest)

        // then
        HrDepartment.processNextRequest(hrEmployeeNumber)

        // then
        verify { certificateRequest.process(hrEmployeeNumber) }
    }

    /* Вспомогательная функция тестирования метода receiveRequest */
    fun receiveRequest(dayOfWeek: DayOfWeek, certificateType: CertificateType): CertificateRequest {
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns certificateType
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        return certificateRequest
    }
}