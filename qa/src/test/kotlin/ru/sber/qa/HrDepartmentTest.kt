package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class HrDepartmentTest {


    @Test
    fun weekendDayTest() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2022-09-11T10:00:00.00Z"), ZoneOffset.UTC)
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun dayIsTuesdayAndNDFLTypeTest() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2022-09-06T07:00:00.00Z"), ZoneOffset.UTC)
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun dayIsMondayAndLabourBookTypeTest() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2022-09-07T19:00:00.00Z"), ZoneOffset.UTC)
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2022-09-07T19:00:00.00Z"), ZoneOffset.UTC)
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequestTest() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2022-09-07T19:00:00.00Z"), ZoneOffset.UTC)
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { certificateRequest.process(3) } returns mockk<Certificate>()

        HrDepartment.receiveRequest(certificateRequest)

        assertDoesNotThrow { HrDepartment.processNextRequest(3) }
        verify { certificateRequest.process(3) }
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}