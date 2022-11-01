package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.assertFailsWith


class HrDepartmentTest {

    private val employeeNumber: Long = 10
    private val certRequestMock = mockk<CertificateRequest>()
    private val certMock = mockk<Certificate>()

    @BeforeEach
    fun setUp() {
        every { certRequestMock.process(employeeNumber) } returns certMock
    }

    @Test
    fun testReceiveRequesSunday() {
        val instant = LocalDateTime.of(2022, 10, 30, 15, 8, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(instant, ZoneId.of("UTC"))
        assertFailsWith(
            WeekendDayException::class
        ) { HrDepartment.receiveRequest(certRequestMock) }
    }

    @Test
    fun testReceiveRequestException() {
        val instant = LocalDateTime.of(2022, 10, 27, 15, 8, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(instant, ZoneId.of("UTC"))
        every { certRequestMock.certificateType } returns CertificateType.NDFL
        assertFailsWith(
            NotAllowReceiveRequestException::class
        ) { HrDepartment.receiveRequest(certRequestMock) }
    }

    @Test
    fun testReceiveRequest() {
        val instant = LocalDateTime.of(2022, 10, 31, 15, 8, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(instant, ZoneId.of("UTC"))
        every { certRequestMock.certificateType } returns CertificateType.NDFL
        HrDepartment.receiveRequest(certRequestMock)
        HrDepartment.processNextRequest(employeeNumber)
        verify(exactly = 1) { certRequestMock.process(employeeNumber) }
    }

}