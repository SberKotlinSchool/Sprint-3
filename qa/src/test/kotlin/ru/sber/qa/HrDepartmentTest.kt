package ru.sber.qa

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal class HrDepartmentTest {
    @MockK
    private lateinit var certificateRequest: CertificateRequest

    private val clock = Clock.fixed(
        Instant.parse("2022-11-01T23:00:00.00Z"), ZoneId.of("Europe/Moscow"))

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
        spyk<HrDepartment>()
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
    fun receiveRequestNegativeTest2() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestNegativeTest3() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestPositiveTest1() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestPositiveTest2() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun processNextRequest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        HrDepartment.receiveRequest(certificateRequest)

        every { certificateRequest.process(any()) } returns Certificate(certificateRequest, 123L, ByteArray(1))
        HrDepartment.processNextRequest(123L)
        verify {
            certificateRequest.process(123L)
        }
    }
}