package ru.sber.qa

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.sber.qa.CertificateType.*
import java.time.Clock
import java.time.DayOfWeek.*
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class HrDepartmentTest {

    @MockK
    private lateinit var certRequest: CertificateRequest

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
    }

    @Test
    fun `getting WeekendDayException`() {
        val exceptionMsg = "Заказ справков в выходной день не работает"
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns SUNDAY
        assertThrows<WeekendDayException>(exceptionMsg) {
            HrDepartment.receiveRequest(certRequest)
        }
    }

    @Test
    fun `getting NotAllowReceiveRequestException`() {
        val exceptionMsg = "Не разрешено принять запрос на справку"
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returnsMany listOf(TUESDAY, MONDAY)
        every { certRequest.certificateType } returnsMany listOf(NDFL, LABOUR_BOOK)
        assertThrows<NotAllowReceiveRequestException>(exceptionMsg) {
            HrDepartment.receiveRequest(certRequest)
        }
        assertThrows<NotAllowReceiveRequestException>(exceptionMsg) {
            HrDepartment.receiveRequest(certRequest)
        }
    }

    @Test
    fun `success request receiving`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns MONDAY
        every { certRequest.certificateType } returns NDFL
        assertDoesNotThrow {
            HrDepartment.receiveRequest(certRequest)
        }
    }

    @Test
    fun `processing requests in (not)right order`() {
        val certRequest2 = mockk<CertificateRequest>()
        val hrEmployeeNumber = 1L
        val cert = mockk<Certificate>()
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns MONDAY
        certRequest.run {
            every { certificateType } returns NDFL
            every { process(any()) } returns cert
            HrDepartment.receiveRequest(this)
        }
        certRequest2.run {
            every { certificateType } returns NDFL
            every { process(any()) } returns cert
            HrDepartment.receiveRequest(this)
        }
        assertDoesNotThrow { HrDepartment.processNextRequest(hrEmployeeNumber) }
        verify(exactly = 1) { certRequest2.process(hrEmployeeNumber) }
        assertDoesNotThrow { HrDepartment.processNextRequest(hrEmployeeNumber) }
        verify(exactly = 1) { certRequest.process(hrEmployeeNumber) }
    }
}