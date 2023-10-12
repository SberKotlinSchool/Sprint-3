package ru.sber.qa.ru.sber.qa

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkStatic
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.LinkedList
import kotlin.random.Random
import kotlin.test.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import ru.sber.qa.Certificate
import ru.sber.qa.CertificateRequest
import ru.sber.qa.CertificateType
import ru.sber.qa.HrDepartment
import ru.sber.qa.NotAllowReceiveRequestException
import ru.sber.qa.WeekendDayException


class HrDepartmentTest {
    private val service = HrDepartment
    private val weekDayListNdfl = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
    private val weekDayListBook = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
    private val weekendDayList = listOf(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY)

    @Test
    fun `receiveRequest should throw WeekendDayException if current day is SUNDAY or SATURDAY`() {
        val certificateRequest = mockkClass(CertificateRequest::class)
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(service.clock).dayOfWeek } returns weekendDayList.random()
        assertThrows<WeekendDayException> { service.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest should throw NotAllowReceiveRequestException if certificate type is NDFL and current day is not weekday`() {
        val certificateRequest = mockkClass(CertificateRequest::class)
        mockkStatic(LocalDateTime::class)
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { LocalDateTime.now(service.clock).dayOfWeek } returns weekDayListBook.random()
        assertThrows<NotAllowReceiveRequestException> { service.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest should throw NotAllowReceiveRequestException if certificate type is LABOUR_BOOK and current day is not weekday`() {
        val certificateRequest = mockkClass(CertificateRequest::class)
        mockkStatic(LocalDateTime::class)
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        every { LocalDateTime.now(service.clock).dayOfWeek } returns weekDayListNdfl.random()
        assertThrows<NotAllowReceiveRequestException> { service.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest should push certificate request into incomeBox if certificate type is NDFL and current day is weekday`() {
        val certificateRequest = mockkClass(CertificateRequest::class)
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(service.clock).dayOfWeek } returns weekDayListNdfl.random()
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        service.receiveRequest(certificateRequest)
        val incomeBox = service::class.java.getDeclaredField("incomeBox")
        incomeBox.isAccessible = true
        assertEquals((incomeBox.get(service) as LinkedList<*>).first, certificateRequest)
    }

    @Test
    fun `receiveRequest should push certificate request into incomeBox if certificate type is LABOUR_BOOK and current day is weekday`() {
        val certificateRequest = mockkClass(CertificateRequest::class)
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(service.clock).dayOfWeek } returns weekDayListBook.random()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        service.receiveRequest(certificateRequest)
        val incomeBox = service::class.java.getDeclaredField("incomeBox")
        incomeBox.isAccessible = true
        assertEquals((incomeBox.get(service) as LinkedList<*>).first, certificateRequest)
    }

    @Test
    fun `processNextRequest should return error if `() {
        val hrEmployeeNumber = Random.nextLong()
        val certificate = mockkClass(Certificate::class)
        val certificateRequest = mockkClass(CertificateRequest::class)

        every { certificateRequest.process(any()) } returns certificate
        val fieldBox = service.javaClass.getDeclaredField("incomeBox")
        fieldBox.trySetAccessible()
        val incomeBox = fieldBox.get(service) as LinkedList<CertificateRequest>
        incomeBox.push(certificateRequest)

        service.processNextRequest(hrEmployeeNumber)
        val fieldOutcome = service.javaClass.getDeclaredField("outcomeOutcome")
        fieldOutcome.trySetAccessible()
        val outcomeList = fieldOutcome.get(service) as LinkedList<Certificate>

        assertEquals(certificate, outcomeList.poll())
    }
}