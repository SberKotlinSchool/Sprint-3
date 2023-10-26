package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset


class HrDepartmentTest {
    private val request = CertificateRequest(1L, CertificateType.NDFL)

    @Test
    fun receiveRequestWithWeekendDayException() {
        val dateTime = LocalDateTime.of(2023, 10, 1, 15, 0)
        HrDepartment.clock = Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun receiveRequestWithNotAllowReceiveRequestException() {
        val dateTime = LocalDateTime.of(2023, 10, 3, 15, 0)
        HrDepartment.clock = Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

    @Test
    fun receiveRequestWithoutException() {
        commonReceiveRequest()
    }

    @Test
    fun processNextRequestWithScanTimeoutException() {
        commonReceiveRequest()
        mockkObject(Scanner)
        every { Scanner.getScanData() } throws ScanTimeoutException()
        assertThrows<ScanTimeoutException> { HrDepartment.processNextRequest(1L) }
    }

    @Test
    fun processNextRequestWithNPE() {
        assertThrows<NullPointerException> { HrDepartment.processNextRequest(1L) }
    }

    @Test
    fun processNextRequestWithoutException() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns byteArrayOf(12, 13, 14)
        commonReceiveRequest()
        HrDepartment.processNextRequest(2L)
    }

    private fun commonReceiveRequest() {
        val dateTime = LocalDateTime.of(2023, 10, 2, 15, 0)
        HrDepartment.clock = Clock.fixed(dateTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)
        HrDepartment.receiveRequest(request)
    }
}