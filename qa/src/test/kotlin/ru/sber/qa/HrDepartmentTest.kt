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

        val certificateRequest =
            CertificateRequest(employeeNumber = 1, certificateType = CertificateType.NDFL)

        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }


        HrDepartment.processNextRequest(1)
    }

    @Test
    fun receiveRequestLabourBook() {
        every { mockClock.instant() } returns Instant.parse("2023-10-05T12:00:00Z")
        every { mockClock.zone } returns ZoneId.of("Europe/Moscow")

        HrDepartment.clock = mockClock

        val certificateRequest =
            CertificateRequest(employeeNumber = 1, certificateType = CertificateType.LABOUR_BOOK)

        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }

        HrDepartment.processNextRequest(1)

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
}