package ru.sber.qa

import io.mockk.*
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.lang.reflect.Field
import java.time.*
import java.util.*
import kotlin.test.assertFailsWith

class HrDepartmentTest {

    private val certificateRequest = mockk<CertificateRequest>()

    @Test
    fun receiveRequestOnWeekendTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SUNDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertFailsWith<WeekendDayException>(block = { HrDepartment.receiveRequest(certificateRequest) })

        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun receiveNDFLRequestOnWrongDayTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertFailsWith<NotAllowReceiveRequestException>(block = { HrDepartment.receiveRequest(certificateRequest) })

        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun receiveLaborBookRequestOnWrongDayTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertFailsWith<NotAllowReceiveRequestException>(block = { HrDepartment.receiveRequest(certificateRequest) })

        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun processNextRequestTest() {
        mockkStatic(LocalDateTime::class)

        val certificateRequest = mockk<CertificateRequest>()
        val certificate = mockk<Certificate>()

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY

        every { certificateRequest.process(any()) } returns certificate

        val incomeBoxField: Field = HrDepartment::class.java.getDeclaredField("incomeBox")
        incomeBoxField.isAccessible = true
        val incomeBox = incomeBoxField.get(HrDepartment) as LinkedList<CertificateRequest>
        incomeBox.push(certificateRequest)

        HrDepartment.processNextRequest(13L)

        verify { certificateRequest.process(13L) }

        val outcomeOutcomeField: Field = HrDepartment::class.java.getDeclaredField("outcomeOutcome")
        outcomeOutcomeField.isAccessible = true
        val outcomeOutcome = outcomeOutcomeField.get(HrDepartment) as LinkedList<Certificate>
        assertTrue(outcomeOutcome.contains(certificate))

        unmockkStatic(LocalDateTime::class)
    }
}