package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("UNCHECKED_CAST")
class HrDepartmentTest {

    private val mockClock = mockk<Clock>()

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns byteArrayOf(1)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun receiveRequestNdfl() {
        every { mockClock.instant() } returns Instant.parse("2023-10-06T12:00:00Z")
        every { mockClock.zone } returns ZoneId.of("Europe/Moscow")

        HrDepartment.clock = mockClock
        HrDepartment.receiveRequest(CertificateRequest(employeeNumber = 1, certificateType = CertificateType.NDFL))

        assertDoesNotThrow {
            HrDepartment.receiveRequest(CertificateRequest(employeeNumber = 1, certificateType = CertificateType.NDFL))
        }

        val incomeBox: LinkedList<CertificateRequest> =
            HrDepartment.getPrivateField("incomeBox") as LinkedList<CertificateRequest>
        val certificateRequest = incomeBox.poll()
        assertEquals(1, certificateRequest.employeeNumber)
        assertEquals(CertificateType.NDFL, certificateRequest.certificateType)
    }

    @Test
    fun receiveRequestLabourBook() {
        every { mockClock.instant() } returns Instant.parse("2023-10-05T12:00:00Z")
        every { mockClock.zone } returns ZoneId.of("Europe/Moscow")

        HrDepartment.clock = mockClock

        assertDoesNotThrow {
            HrDepartment.receiveRequest(
                CertificateRequest(
                    employeeNumber = 2,
                    certificateType = CertificateType.LABOUR_BOOK
                )
            )
        }

        val incomeBox: LinkedList<CertificateRequest> =
            HrDepartment.getPrivateField("incomeBox") as LinkedList<CertificateRequest>
        val certificateRequest = incomeBox.poll()
        assertEquals(2, certificateRequest.employeeNumber)
        assertEquals(CertificateType.LABOUR_BOOK, certificateRequest.certificateType)
    }

    @Test
    fun receiveRequestWeekendDayException() {
        every { mockClock.instant() } returns Instant.parse("2023-10-08T12:00:00Z")
        every { mockClock.zone } returns ZoneId.of("Europe/Moscow")

        HrDepartment.clock = mockClock

        val certificateRequest =
            CertificateRequest(employeeNumber = 1, certificateType = CertificateType.LABOUR_BOOK)

        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestNotAllowReceiveRequestException() {
        every { mockClock.instant() } returns Instant.parse("2023-10-04T12:00:00Z")
        every { mockClock.zone } returns ZoneId.of("Europe/Moscow")

        HrDepartment.clock = mockClock

        val certificateRequest =
            CertificateRequest(employeeNumber = 1, certificateType = CertificateType.LABOUR_BOOK)

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun processNextRequest() {
        every { mockClock.instant() } returns Instant.parse("2023-10-04T12:00:00Z")
        every { mockClock.zone } returns ZoneId.of("Europe/Moscow")

        HrDepartment.clock = mockClock

        val certificateRequest =
            CertificateRequest(employeeNumber = 1, certificateType = CertificateType.LABOUR_BOOK)

        val incomeBox: LinkedList<CertificateRequest> =
            HrDepartment.getPrivateField(fieldName = "incomeBox") as LinkedList<CertificateRequest>
        incomeBox.push(certificateRequest)

        assertEquals(1, incomeBox.size)

        HrDepartment.processNextRequest(hrEmployeeNumber = 1)

        assertEquals(0, incomeBox.size)

        val outcomeOutcome: LinkedList<Certificate> =
            HrDepartment.getPrivateField(fieldName = "outcomeOutcome") as LinkedList<Certificate>

        assertEquals(1, outcomeOutcome.size)

        val certificate = outcomeOutcome.poll()

        assertTrue {
            Certificate(
                certificateRequest = certificateRequest,
                processedBy = 1,
                data = byteArrayOf(1)
            ) == certificate
        }
    }

    private fun HrDepartment.getPrivateField(fieldName: String): Any {
        return javaClass.getDeclaredField(fieldName).let {
            it.isAccessible = true
            return@let it.get(this);
        }
    }
}