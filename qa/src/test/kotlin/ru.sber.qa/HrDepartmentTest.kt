package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.test.assertContains
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    @Test
    fun `getReceiveRequest with WeekendDayException, SATURDAY`() {
        mockkStatic(LocalDateTime::class)
        val certificateRequest = mockk<CertificateRequest>()
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SATURDAY
        assertFailsWith<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `getReceiveRequest with WeekendDayException, SUNDAY`() {
        mockkStatic(LocalDateTime::class)
        val certificateRequest = mockk<CertificateRequest>()
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SUNDAY
        assertFailsWith<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `getReceiveRequest with NotAllowReceiveRequestException, LABOUR_BOOK, MONDAY`() {
        mockkStatic(LocalDateTime::class)
        val employeeNumber: Long = 1
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        assertFailsWith<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `getReceiveRequest with NotAllowReceiveRequestException, NDFL, TUESDAY`() {
        mockkStatic(LocalDateTime::class)
        val employeeNumber: Long = 1
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        assertFailsWith<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `getReceiveRequest success, NDFL, MONDAY`() {
        mockkStatic(LocalDateTime::class)
        val employeeNumber: Long = 1
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `getReceiveRequest success, LABOUR_BOOK, TUESDAY`() {
        mockkStatic(LocalDateTime::class)
        val employeeNumber: Long = 1
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}