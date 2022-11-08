package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal class HrDepartmentTest {

    @Test
    fun receiveRequestForSunday() {
        val sunday = LocalDateTime.of(2022, 11, 6, 0, 0, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(sunday, ZoneId.of("UTC"))
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(mockk()) }
    }

    @Test
    fun receiveRequestForSaturday() {
        val saturday = LocalDateTime.of(2022, 11, 5, 0, 0, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(saturday, ZoneId.of("UTC"))
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(mockk()) }
    }

    @Test
    fun receiveRequestFailTest() {
        val thursday = LocalDateTime.of(2022, 11, 3, 0, 0, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(thursday, ZoneId.of("UTC"))
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestSuccessTest() {
        val tuesday = LocalDateTime.of(2022, 11, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(tuesday, ZoneId.of("UTC"))
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequestTest() {
        assertThrows<NullPointerException> {
            HrDepartment.processNextRequest(1L)
        }
    }
}