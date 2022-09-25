package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek
import java.time.LocalDateTime

internal class HrDepartmentTest {
    private val hrDepartment = HrDepartment
    private val employeeNumber = 10L
    private lateinit var certificateRequest: CertificateRequest

    @Test
    fun receiveRequestWhenWeekend() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(hrDepartment.clock).dayOfWeek } returns DayOfWeek.SUNDAY
        certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)

        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWhenNotAllowException() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(hrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)

        assertThrows<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestWhenOk() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(hrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)

        assertDoesNotThrow { hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(hrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY

        certificateRequest = mockk()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        every { certificateRequest.process(employeeNumber) }.returns(mockk())

        hrDepartment.receiveRequest(certificateRequest)
        assertDoesNotThrow { hrDepartment.processNextRequest(employeeNumber) }
        verify { certificateRequest.process(employeeNumber) }
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}