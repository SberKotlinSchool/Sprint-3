package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HrDepartmentTest {

    @AfterEach
    fun clearAllLists() {
        val incomeBox = getPrivateFieldValueFromClassByName("incomeBox") as? MutableList<*>
        val outcomeOutcome = getPrivateFieldValueFromClassByName("outcomeOutcome") as? MutableList<*>

        incomeBox?.clear()
        outcomeOutcome?.clear()
    }

    @Test
    fun `receiveRequest should throw WeekendDayException if current day is weekend`() {
        // given
        HrDepartment.clock = buildFixedClockWithDayOfWeek(DayOfWeek.SUNDAY)
        val request = mockk<CertificateRequest>()

        // then
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun `receiveRequest should throw NotAllowReceiveRequestException if current day is Tuesday and certificate type is NDFL`() {
        // given
        HrDepartment.clock = buildFixedClockWithDayOfWeek(DayOfWeek.TUESDAY)
        val request = mockk<CertificateRequest>()

        every { request.certificateType } returns CertificateType.NDFL

        // then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun `receiveRequest should throw NotAllowReceiveRequestException if current day is Monday and certificate type is LABOUR_BOOK`() {
        // given
        HrDepartment.clock = buildFixedClockWithDayOfWeek(DayOfWeek.MONDAY)
        val request = mockk<CertificateRequest>()

        every { request.certificateType } returns CertificateType.LABOUR_BOOK

        // then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun `receiveRequest should put request into income box`() {
        // given
        HrDepartment.clock = buildFixedClockWithDayOfWeek(DayOfWeek.MONDAY)
        val request = mockk<CertificateRequest>()

        every { request.certificateType } returns CertificateType.NDFL

        // when
        HrDepartment.receiveRequest(request)
        val incomeBox = getPrivateFieldValueFromClassByName("incomeBox") as? List<*>

        // then
        assertNotNull(incomeBox)
        assertTrue(incomeBox.isNotEmpty())
    }

    @Test
    fun `processRequest should throw exception if there aren't any certificate requests in income box`() {
        // given
        val hrNumber = 1L

        // then
        assertThrows<NullPointerException> { HrDepartment.processNextRequest(hrNumber) }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `processRequest should put ready certificate into outcome`() {
        // given
        val request = mockk<CertificateRequest>()
        val certificate = mockk<Certificate>()
        val hrNumber = 1L
        val incomeBox = getPrivateFieldValueFromClassByName("incomeBox") as? MutableList<CertificateRequest>
        incomeBox?.add(request)

        every { request.process(hrNumber) } returns certificate

        // when
        HrDepartment.processNextRequest(hrNumber)
        val outcomeOutcome = getPrivateFieldValueFromClassByName("outcomeOutcome") as? List<*>

        // then
        assertNotNull(outcomeOutcome)
        assertTrue(outcomeOutcome.isNotEmpty())
        assertEquals(certificate, outcomeOutcome.firstOrNull())
    }

    companion object {

        private fun buildFixedClockWithDayOfWeek(dayOfWeek: DayOfWeek) =
            LocalDate.now()
                .with(dayOfWeek)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .let {
                    Clock.fixed(it, ZoneId.systemDefault())
                }

        private fun getPrivateFieldValueFromClassByName(name: String) =
            HrDepartment::class.java.declaredFields
                .find { it.name == name }
                ?.also { it.isAccessible = true }
                ?.get(HrDepartment::class.java)
    }

}