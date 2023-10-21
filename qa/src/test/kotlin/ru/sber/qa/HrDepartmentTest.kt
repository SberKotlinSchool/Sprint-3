package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.DayOfWeek
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HrDepartmentTest {

    private val certificateRequest = mockk<CertificateRequest>()

    @BeforeAll
    fun beforeAll() {
        unmockkAll()
        mockkStatic(LocalDateTime::class)
    }

    @Test
    fun testReceiveRequestCertificateTypeNdflAllowed() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun testReceiveRequestCertificateTypeNdflNotAllowed() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun testReceiveRequestCertificateTypeLabourBookAllowed() {
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun testReceiveRequestCertificateTypeLabourBookNotAllowed() {
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun testReceiveRequestIllegalDayOfWeek() {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SUNDAY
        assertThrows(WeekendDayException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun testProcessNextRequest() {
        HrDepartment.receiveRequest(certificateRequest)
        every { certificateRequest.process(any()) } returns mockk()
        HrDepartment.processNextRequest(1L)
        verify { certificateRequest.process(1L) }
    }

}