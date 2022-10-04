package ru.sber.qa

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class HrDepartmentTest {

    private val hrDepartment = HrDepartment
    private val employeeNumber = 1L

    @BeforeEach
    fun `give mockk LocalDateTime`() {
        //give
        mockkStatic(LocalDateTime::class)
    }

    @Test
    fun `positive ReceiveRequest with parameters TUESDAY and LABOUR_BOOK Test` () {
        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        //then
        assertDoesNotThrow{
            hrDepartment.receiveRequest(CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK))
        }
    }

    @Test
    fun `negative ReceiveRequest with parameters TUESDAY and NDFL Test`() {
        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.SATURDAY
        //then
        assertThrows(WeekendDayException::class.java){
            hrDepartment.receiveRequest(CertificateRequest(employeeNumber, CertificateType.NDFL))
        }
    }

    @Test
    fun `NotAllowReceiveRequestException with parameters TUESDAY and NDFL receiveRequest Test`() {
        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        //then
        assertThrows(NotAllowReceiveRequestException::class.java){
            hrDepartment.receiveRequest(CertificateRequest(employeeNumber, CertificateType.NDFL))
        }
    }

    @Test
    fun `NotAllowReceiveRequestException MONDAY and LABOUR_BOOK receiveRequest Test`() {
        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY
        //then
        assertThrows(NotAllowReceiveRequestException::class.java){
            hrDepartment.receiveRequest(CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK))
        }
    }

    @Test
    fun `processNextRequest positive`() {
        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY;
        hrDepartment.receiveRequest(CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK))
        //then
        assertDoesNotThrow {
            hrDepartment.processNextRequest(employeeNumber)
        }
    }
}