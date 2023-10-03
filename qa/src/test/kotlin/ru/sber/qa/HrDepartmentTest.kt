package ru.sber.qa

import io.mockk.*
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class HrDepartmentTest {

    private val certificateRequest = mockk<CertificateRequest>()

    @Test
    fun receiveRequestOnWeekendTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SUNDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertFailsWith<WeekendDayException>(block = { HrDepartment.receiveRequest(certificateRequest) })

        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun receiveNDFLRequestOnWrongDayTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertFailsWith<NotAllowReceiveRequestException>(block = { HrDepartment.receiveRequest(certificateRequest) })

        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun receiveLaborBookRequestOnWrongDayTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertFailsWith<NotAllowReceiveRequestException>(block = { HrDepartment.receiveRequest(certificateRequest) })

        unmockkStatic(LocalDateTime::class)
    }
}