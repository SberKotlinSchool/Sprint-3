package ru.sber.qa

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime

internal class HrDepartmentTest {
    private val hrDepartment = HrDepartment
    @Test
    fun `positive receiveRequest Test`() {

    }

    @Test
    fun `WeekendDayException receiveRequest Test`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.SATURDAY
        assertThrows(WeekendDayException::class.java){
            hrDepartment.receiveRequest(CertificateRequest(1, CertificateType.NDFL))
        }
    }

    @Test
    fun `NotAllowReceiveRequestException receiveRequest Test`() {

    }

    @Test
    fun processNextRequest() {
    }
}