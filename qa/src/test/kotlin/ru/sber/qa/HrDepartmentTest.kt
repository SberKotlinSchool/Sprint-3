package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*

class HrDepartmentTest {

    private var certificateRequest: CertificateRequest = mockk()

    @Test
    fun testReceiveRequestWeekendDayException() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-07T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        assertThrows(WeekendDayException::class.java) { HrDepartment.receiveRequest(certificateRequest)}
    }

    @Test
    fun testReceiveRequestNotAllowReceiveRequestException() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-10T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-09T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun testReceiveRequestWhenNotWeekendDayAndCertificateTypeIsNDFL() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-09T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        val incomeBoxList = getIncomeBoxInHrDepartment()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        HrDepartment.receiveRequest(certificateRequest)
        assertEquals(certificateRequest, incomeBoxList.first)
    }

    @Test
    fun testReceiveRequestWhenNotWeekendDayAndCertificateTypeIsLabourBook() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-10T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        val incomeBoxList = getIncomeBoxInHrDepartment()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        HrDepartment.receiveRequest(certificateRequest)
        assertEquals(certificateRequest, incomeBoxList.first)
    }

    @Test
    fun testProcessNextRequest() {
        val certificate = mockk<Certificate>()
        every { certificateRequest.process(1L) } returns certificate
        val outcomeOutcome: LinkedList<Certificate> = LinkedList()
        val incomeBoxList = getIncomeBoxInHrDepartment() as LinkedList<CertificateRequest>
        incomeBoxList.push(certificateRequest)
        var outcomeOutcomeList: LinkedList<Certificate>
        HrDepartment.javaClass.getDeclaredField("outcomeOutcome").let {
            it.isAccessible = true
            outcomeOutcomeList = it.get(outcomeOutcome) as LinkedList<Certificate>
        }
        HrDepartment.processNextRequest(1L)
        assertEquals(certificate, outcomeOutcomeList.first)
    }

    private fun getIncomeBoxInHrDepartment(): LinkedList<*> {
        val incomeBox: LinkedList<CertificateRequest> = LinkedList()
        val incomeBoxList: LinkedList<*>
        HrDepartment.javaClass.getDeclaredField("incomeBox").let {
            it.isAccessible = true
            incomeBoxList = it.get(incomeBox) as LinkedList<*>
        }
        return incomeBoxList
    }
}