package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime


internal class HrDepartmentTest {

    val certificateRequest = mockk<CertificateRequest>()
    val clock = mockk<Clock>()
    val localDateTime = mockk<LocalDateTime>()

    @BeforeEach
    fun setup() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(clock) } returns localDateTime
        HrDepartment.clock = clock
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestNdflNormalTest() {
        every { localDateTime.dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertDoesNotThrow{ HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestNdflTuesdayExceptionTest() {
        every { localDateTime.dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertThrows(NotAllowReceiveRequestException::class.java){ HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLabourBookTuesdayNormalTest() {
        every { localDateTime.dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertDoesNotThrow{ HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLabourBookFridayExceptionTest() {
        every { localDateTime.dayOfWeek } returns DayOfWeek.FRIDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertThrows(NotAllowReceiveRequestException::class.java){ HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestSundayExceptionTest() {
        every { localDateTime.dayOfWeek } returns DayOfWeek.SUNDAY

        assertThrows(WeekendDayException::class.java){ HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequest() {
        val certificate = mockk<Certificate>()
        every { localDateTime.dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { certificateRequest.process(any()) } returns certificate
        HrDepartment.receiveRequest(certificateRequest)

        assertDoesNotThrow { HrDepartment.processNextRequest(11L) }
    }
}