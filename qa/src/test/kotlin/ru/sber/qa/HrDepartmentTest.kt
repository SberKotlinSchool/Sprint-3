package ru.sber.qa

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal class HrDepartmentTest {
    private val employeeNumber = 123L
    private val certificateType = CertificateType.LABOUR_BOOK

    private val clock = Clock.fixed(
        Instant.parse("2022-11-01T23:00:00.00Z"), ZoneId.of("Europe/Moscow"))

    private val certificateRequest = CertificateRequest(employeeNumber, certificateType)

    @BeforeEach
    fun init() {
    }

    @Test
    fun getClock() {
        mockkStatic(Clock::class)
        every { Clock.systemUTC() } returns clock
        assertEquals(clock, HrDepartment.clock)
    }

    @Test
    fun setClock() {
        HrDepartment.clock = clock
        assertEquals(clock, HrDepartment.clock)
    }

    @Test
    fun receiveRequestNegativeTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SATURDAY
        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun processNextRequest() {
    }

}