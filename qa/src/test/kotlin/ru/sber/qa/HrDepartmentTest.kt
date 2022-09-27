package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import java.time.DayOfWeek
import java.time.LocalDateTime

@DisplayName("HrDepartment class test cases")
internal class HrDepartmentTest {

    @BeforeEach
    fun mockLocalDateTime() {
        mockkStatic(LocalDateTime::class)
    }

    @Test
    fun `HrDepartment receiveRequest() test with holiday weekday for WeekendDayException`() {

        val hrDepartment = HrDepartment
        val weekendDay = DayOfWeek.SUNDAY
        val employeeNumber: Long = 251
        val certificateRequestNdfl = CertificateRequest(employeeNumber, CertificateType.NDFL)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns weekendDay
        assertThrows(WeekendDayException::class.java) {
            hrDepartment.receiveRequest(certificateRequestNdfl)
        }

    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with labour request allowed`() {
        val hrDepartment = HrDepartment
        val workDayForLabourBookRequest = DayOfWeek.THURSDAY
        val employeeNumber: Long = 251
        val certificateRequestLabourBook = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForLabourBookRequest
        hrDepartment.receiveRequest(certificateRequestLabourBook)
    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with no labour request allowed for NotAllowReceiveRequestException`() {

        val hrDepartment = HrDepartment
        val workDayForNdflRequest = DayOfWeek.FRIDAY
        val employeeNumber: Long = 251
        val certificateRequestLabourBook = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForNdflRequest
        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(certificateRequestLabourBook)
        }

    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with ndfl request allowed`() {
        val hrDepartment = HrDepartment
        val employeeNumber: Long = 251
        val workDayForNdflRequest = DayOfWeek.FRIDAY
        val certificateRequestNdfl = CertificateRequest(employeeNumber, CertificateType.NDFL)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForNdflRequest
        hrDepartment.receiveRequest(certificateRequestNdfl)
    }

    @Test
    fun `HrDepartment receiveRequest() test with workday with no ndfl request allowed for NotAllowReceiveRequestException`() {
        val hrDepartment = HrDepartment
        val employeeNumber: Long = 251
        val certificateRequestNdfl = CertificateRequest(employeeNumber, CertificateType.NDFL)
        val workDayForLabourBookRequest = DayOfWeek.THURSDAY

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForLabourBookRequest
        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(certificateRequestNdfl)
        }
    }

    @Test
    fun `HrDepartment processNextRequest() test with mocks`() {
        //TODO("Возможно есть какая возможнось мокировать private fields в классе, но пока не разобрался, было бы сильно красивее")

        val hrDepartment = HrDepartment
        val employeeNumber: Long = 251
        val certificateRequestNdfl = CertificateRequest(employeeNumber, CertificateType.NDFL)
        val workDayForNdflRequest = DayOfWeek.FRIDAY

        mockkObject(certificateRequestNdfl)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns workDayForNdflRequest
        every { certificateRequestNdfl.process(employeeNumber) } returns Certificate(
            certificateRequestNdfl, employeeNumber,
            byteArrayOf(100)
        )
        hrDepartment.receiveRequest(certificateRequestNdfl)
        assertDoesNotThrow { HrDepartment.processNextRequest(employeeNumber) }
        verify { certificateRequestNdfl.process(employeeNumber) }
        unmockkObject(certificateRequestNdfl)
    }

    @AfterEach
    fun unlockAllLocalDateTime() {
        unmockkAll()
    }

}