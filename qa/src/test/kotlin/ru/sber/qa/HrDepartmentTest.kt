package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class HrDepartmentTest {

    @BeforeEach
    fun setUp() {
        mockkObject(HrDepartment)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test receiveRequest with NDFL on a valid day`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-09T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.NDFL)

        HrDepartment.receiveRequest(certificateRequest)

        val incomeBox = getPrivateFieldValue<List<CertificateRequest>>("incomeBox")
        assertTrue(incomeBox.contains(certificateRequest))
    }

    @Test
    fun `test receiveRequest with Labour Book on a valid day`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-12T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.LABOUR_BOOK)

        HrDepartment.receiveRequest(certificateRequest)

        val incomeBox = getPrivateFieldValue<List<CertificateRequest>>("incomeBox")
        assertTrue(incomeBox.contains(certificateRequest))
    }

    @Test
    fun `test receiveRequest on a weekend day`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-15T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.NDFL)

        val exception = assertThrows(WeekendDayException::class.java) {
            HrDepartment.receiveRequest(certificateRequest)
        }
        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }

    @Test
    fun `test receiveRequest on a non-allowed day for NDFL`() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-12T10:00:00Z"), ZoneId.of("UTC"))
        val certificateRequest = CertificateRequest(12345, CertificateType.NDFL)

        val exception = assertThrows(NotAllowReceiveRequestException::class.java) {
            HrDepartment.receiveRequest(certificateRequest)
        }
        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

    private fun <T> getPrivateFieldValue(fieldName: String): T {
        val field = HrDepartment::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(HrDepartment) as T
    }

    @Test
    fun `processNextRequest should handle request`() {
        val certificateRequest = CertificateRequest(1, CertificateType.NDFL)
        HrDepartment.clock = mockk()
        val incomeBox = getPrivateFieldValue<List<CertificateRequest>>("incomeBox")
        val outcomeOutcome = getPrivateFieldValue<List<CertificateRequest>>("outcomeOutcome")
        every { certificateRequest.process(any()) } returns Certificate(certificateRequest, 1, byteArrayOf(1, 2, 3))
        HrDepartment.processNextRequest(1)
        assertEquals(0, incomeBox.size)
        assertEquals(1, outcomeOutcome.size)
    }

}