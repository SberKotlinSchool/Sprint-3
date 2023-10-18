package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.util.*

class HrDepartmentTest {
    @AfterEach
    fun clearAllLists() {
        val incomeBox = HrDepartment::class.java.getDeclaredField("incomeBox").also { it.isAccessible = true }.get(HrDepartment::class.java) as LinkedList<*>
        val outcomeBox = HrDepartment::class.java.getDeclaredField("outcomeOutcome").also { it.isAccessible = true }.get(HrDepartment::class.java) as LinkedList<*>

        incomeBox.clear()
        outcomeBox.clear()
    }
    @Test
    fun `should throw WeekendDayException if current day is weekend`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-15T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        val request = mockk<CertificateRequest>()

        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun `should throw NotAllowReceiveRequestException if current day is Tuesday and certificate type is NDFL`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-12T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        val request = mockk<CertificateRequest>()

        every { request.certificateType } returns CertificateType.NDFL

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun `should throw NotAllowReceiveRequestException if current day is Monday and certificate type is LABOUR_BOOK`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-09T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        val request = mockk<CertificateRequest>()

        every { request.certificateType } returns CertificateType.LABOUR_BOOK

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun `receiveRequest should put request into income box`() {
        // given
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-09T10:00:00.00Z"), ZoneId.of("Europe/Moscow"))
        val request = mockk<CertificateRequest>()

        every { request.certificateType } returns CertificateType.NDFL

        // when
        HrDepartment.receiveRequest(request)
        val incomeBox = HrDepartment::class.java.getDeclaredField("incomeBox").also { it.isAccessible = true }.get(HrDepartment::class.java) as LinkedList<*>
        // then
        assertNotNull(incomeBox)
        assertTrue(incomeBox.isNotEmpty())
    }



    @Test
    fun `processRequest should throw exception if there aren't any certificate requests in income box`() {
        val hrNumber = 1L

        assertThrows<NullPointerException> { HrDepartment.processNextRequest(hrNumber) }
    }

    @Test
    fun `processRequest should put ready certificate into outcome`() {
        val request = mockk<CertificateRequest>()
        val certificate = mockk<Certificate>()
        val hrNumber = 1L

        val incomeBox = HrDepartment::class.java.getDeclaredField("incomeBox").also { it.isAccessible = true }.get(HrDepartment::class.java) as LinkedList<CertificateRequest>
        val outcomeBox = HrDepartment::class.java.getDeclaredField("outcomeOutcome").also { it.isAccessible = true }.get(HrDepartment::class.java) as LinkedList<*>
        incomeBox.add(request)

        every { request.process(hrNumber) } returns certificate

        HrDepartment.processNextRequest(hrNumber)

        assertNotNull(outcomeBox)
        assertTrue(outcomeBox.isNotEmpty())
        assertEquals(certificate, outcomeBox.firstOrNull())
    }


}