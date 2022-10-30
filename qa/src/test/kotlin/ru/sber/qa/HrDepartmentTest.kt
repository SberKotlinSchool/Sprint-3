package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

internal class HrDepartmentTest {

    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
        certificateRequest = mockk()
    }

    @Test
    fun `test receiveRequest throws WeekendDayException`() {
        //given
        val systemClock = mockkClass(Clock::class)

        //when
        every { LocalDateTime.now(systemClock).dayOfWeek } returns DayOfWeek.SUNDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        //then
        assertThrows(WeekendDayException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `test receiveRequest correct output when DayOfWeek equals MONDAY and CertificateType equals NDFL`() {
        //given
        val incomeBox: LinkedList<CertificateRequest> = LinkedList()

        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        HrDepartment.receiveRequest(certificateRequest)

        //then
        HrDepartment.javaClass.getDeclaredField("incomeBox").let {
            it.isAccessible = true
            val incomeBoxList = it.get(incomeBox)
            if (incomeBoxList is LinkedList<*>) {
                assertEquals(certificateRequest, incomeBoxList.first)
            }
        }
    }

    @Test
    fun `test receiveRequest correct output when DayOfWeek equals TUESDAY and CertificateType equals LABOUR_BOOK`() {
        //given
        val incomeBox: LinkedList<CertificateRequest> = LinkedList()

        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        HrDepartment.receiveRequest(certificateRequest)

        //then
        HrDepartment.javaClass.getDeclaredField("incomeBox").let {
            it.isAccessible = true
            val incomeBoxList = it.get(incomeBox)
            if (incomeBoxList is LinkedList<*>) {
                assertEquals(certificateRequest, incomeBoxList.first)
            }
        }
    }

    @Test
    fun `test receiveRequest throws NotAllowReceiveRequestException`() {
        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        //then
        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }

        //when
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        //then
        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `test processNextRequest correct output`() {
        //given
        val certificate = mockk<Certificate>()
        every { certificateRequest.process(0L) } returns certificate
        val incomeBox: LinkedList<CertificateRequest> = LinkedList()
        val outcomeOutcome: LinkedList<Certificate> = LinkedList()
        HrDepartment.javaClass.getDeclaredField("incomeBox").let {
            it.isAccessible = true
            val incomeBoxList = it.get(incomeBox) as LinkedList<CertificateRequest>
            incomeBoxList.push(certificateRequest)
        }

        //then
        HrDepartment.javaClass.getDeclaredField("outcomeOutcome").let {
            it.isAccessible = true
            val outcomeOutcomeList = it.get(outcomeOutcome) as LinkedList<*>

            HrDepartment.processNextRequest(0L)

            assertEquals(certificate, outcomeOutcomeList.first)
        }
    }
}