package ru.sber.qa

import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.every
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.random.Random

class HrDepartmentTest {
    companion object {
        private const val HR_EMPLOYEE_NUMBER = 1234L
    }

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime.now()::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveExceptionIfWeekendDay() {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SATURDAY
        assertThrows(WeekendDayException::class.java) {
            HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK))
        }
    }

    @Test
    fun receiveNotAllowReceiveRequestException() {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.FRIDAY
        assertThrows(NotAllowReceiveRequestException::class.java) {
            HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK))
        }
    }

    @Test
    fun receiveCertificateRequestNDFL() {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.NDFL)) }

    }

    @Test
    fun receiveCertificateRequestLABOUR_BOOK() {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK)) }
    }

    @Test
    fun processNextRequestWithException() {
        mockkObject(Scanner)
        val certificateRequest = mockk<CertificateRequest>()

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        every { certificateRequest.employeeNumber } returns HR_EMPLOYEE_NUMBER
        every { certificateRequest.process(any()) } throws ScanTimeoutException()
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY

        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        assertThrows(ScanTimeoutException::class.java) { HrDepartment.processNextRequest(HR_EMPLOYEE_NUMBER) }
        verify(exactly = 1) { certificateRequest.process(HR_EMPLOYEE_NUMBER) }
    }

    @Test
    fun processNextRequestSuccessfully() {
        mockkObject(Scanner)
        val certificateRequest = mockk<CertificateRequest>()
        val scanData = Random.nextBytes(100)

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        every { certificateRequest.employeeNumber } returns HR_EMPLOYEE_NUMBER
        every { certificateRequest.process(any()) } returns Certificate(certificateRequest, HR_EMPLOYEE_NUMBER, scanData)
        every { Scanner.getScanData() } returns scanData
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY

        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        assertDoesNotThrow { HrDepartment.processNextRequest(HR_EMPLOYEE_NUMBER) }
        verify(exactly = 1) { certificateRequest.process(HR_EMPLOYEE_NUMBER) }
    }
}