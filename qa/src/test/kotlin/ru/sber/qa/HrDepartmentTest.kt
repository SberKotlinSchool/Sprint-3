package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals


internal class HrDepartmentTest {

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestNdflNormalTest() {
        val certificateRequest = mockk<CertificateRequest>()
        val clock = mockk<Clock>()
        val localDateTime = mockk<LocalDateTime>()
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(clock) } returns localDateTime
        HrDepartment.clock = clock

        every { localDateTime.dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertDoesNotThrow{ HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestNdflTuesdayExceptionTest() {
        val certificateRequest = mockk<CertificateRequest>()
        val clock = mockk<Clock>()
        val localDateTime = mockk<LocalDateTime>()

        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(clock) } returns localDateTime
        HrDepartment.clock = clock

        every { localDateTime.dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLabourBookTuesdayNormalTest() {
        val certificateRequest = mockk<CertificateRequest>()
        val clock = mockk<Clock>()
        val localDateTime = mockk<LocalDateTime>()

        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(clock) } returns localDateTime
        HrDepartment.clock = clock

        every { localDateTime.dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertDoesNotThrow{ HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLabourBookFridayExceptionTest() {
        val certificateRequest = mockk<CertificateRequest>()
        val clock = mockk<Clock>()
        val localDateTime = mockk<LocalDateTime>()

        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(clock) } returns localDateTime
        HrDepartment.clock = clock

        every { localDateTime.dayOfWeek } returns DayOfWeek.FRIDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestSundayExceptionTest() {
        val certificateRequest = mockk<CertificateRequest>()
        val clock = mockk<Clock>()
        val localDateTime = mockk<LocalDateTime>()

        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(clock) } returns localDateTime
        HrDepartment.clock = clock

        every { localDateTime.dayOfWeek } returns DayOfWeek.SUNDAY

        assertThrows(WeekendDayException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun processNextRequestTest() {
        val certificate = mockk<Certificate>()
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.process(any()) } returns certificate

        val incomeBoxField = HrDepartment.javaClass.getDeclaredField("incomeBox")
        val outcomeOutcomeField = HrDepartment.javaClass.getDeclaredField("outcomeOutcome")
        incomeBoxField.trySetAccessible()
        outcomeOutcomeField.trySetAccessible()
        val incomeBox = incomeBoxField.get(HrDepartment) as LinkedList<CertificateRequest>
        val outcomeOutcome = outcomeOutcomeField.get(HrDepartment) as LinkedList<Certificate>

        incomeBox.clear()
        incomeBox.add(certificateRequest)

        assertDoesNotThrow { HrDepartment.processNextRequest(11L) }
        assertEquals(1, outcomeOutcome.size)
    }
}