package ru.sber.qa

import io.mockk.mockk
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.LinkedList

class HrDepartmentTest {
    companion object {
        private const val HR_EMPLOYEE_NUMBER = 1234L
    }

    @AfterEach
    fun tearDown(){
        unmockkAll()
    }

    @Test
    fun receiveExceptionIfWeekendDay() {
        mockkStatic(LocalDateTime.now()::class) {
            every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SATURDAY
            assertThrows(WeekendDayException::class.java) {
                HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK))
            }
        }
    }
    @Test
    fun receiveNotAllowReceiveRequestException() {
        mockkStatic(LocalDateTime.now()::class) {
            every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.FRIDAY
            assertThrows(NotAllowReceiveRequestException::class.java) {
                HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK))
            }
        }
    }

    @Test
    fun receiveCertificateRequestNDFL() {
        mockkStatic(LocalDateTime.now()::class) {
            every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
            assertDoesNotThrow { HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.NDFL)) }
        }
    }

    @Test
    fun receiveCertificateRequestLABOUR_BOOK() {
        mockkStatic(LocalDateTime.now()::class) {
            every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
            assertDoesNotThrow { HrDepartment.receiveRequest(CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK)) }
        }
    }

    @Test
    fun processNextRequestSuccessfully() {
        mockkConstructor(LinkedList::class) {
            every { anyConstructed<LinkedList<Any>>().poll() } returns mockk<CertificateRequest>(relaxed = true)
            mockkObject(Scanner)
            every { Scanner.getScanData() } returns ByteArray(100)
            assertDoesNotThrow { HrDepartment.processNextRequest(HR_EMPLOYEE_NUMBER) }
        }
    }

    @Test
    fun processNextRequestWithException() {
        mockkConstructor(LinkedList::class) {
            every { anyConstructed<LinkedList<Any>>().poll() } returns mockk<CertificateRequest> {
                every { process(any()) } throws ScanTimeoutException()
            }
            assertThrows(ScanTimeoutException::class.java) { HrDepartment.processNextRequest(HR_EMPLOYEE_NUMBER) }
        }
    }
}