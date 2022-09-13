package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import java.time.DayOfWeek
import java.time.LocalDateTime

@DisplayName("HrDepartment class test cases")
internal class HrDepartmentTest {

    private val hrDepartment = HrDepartment
    private val employeeNumber: Long = 251

    private val weekendDay = DayOfWeek.SUNDAY
    private val workDayForNdflRequest = DayOfWeek.FRIDAY
    private val workDayForLabourBookRequest = DayOfWeek.THURSDAY

    private val certificateRequestNdfl = CertificateRequest(employeeNumber, CertificateType.NDFL)
    private val certificateRequestLabourBook = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)

    @Test
    fun `HrDepartment receiveRequest() test with holiday weekday for WeekendDayException`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns weekendDay
        assertThrows(WeekendDayException::class.java) {
            hrDepartment.receiveRequest(certificateRequestNdfl)
        }

    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with labour request allowed`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForLabourBookRequest
        hrDepartment.receiveRequest(certificateRequestLabourBook)
    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with no labour request allowed for NotAllowReceiveRequestException`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForNdflRequest
        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(certificateRequestLabourBook)
        }

    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with ndfl request allowed`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForNdflRequest
        hrDepartment.receiveRequest(certificateRequestNdfl)
    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with no ndfl request allowed for NotAllowReceiveRequestException`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForLabourBookRequest
        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(certificateRequestNdfl)
        }
    }

//    @Test
//    fun `HrDepartment processNextRequest() test` () {
//        mockkObject(LinkedList<CertificateRequest>().poll())
//        every { LinkedList<CertificateRequest>().poll() } returns certificateRequestNdfl
//    }

}