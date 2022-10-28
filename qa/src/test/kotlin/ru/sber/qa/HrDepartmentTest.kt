package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HrDepartmentTest {
    private val hrDepartment = HrDepartment

    @AfterAll
    fun unmock(){
        unmockkAll()
    }

    @Test
    fun `positive receiveRequest Test`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        assertDoesNotThrow{
            hrDepartment.receiveRequest(CertificateRequest(1, CertificateType.LABOUR_BOOK))
        }
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
    fun `NotAllowReceiveRequestException TUESDAY and NDFL receiveRequest Test`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        assertThrows(NotAllowReceiveRequestException::class.java){
            hrDepartment.receiveRequest(CertificateRequest(1, CertificateType.NDFL))
        }
    }

    @Test
    fun `NotAllowReceiveRequestException MONDAY and LABOUR_BOOK receiveRequest Test`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY
        assertThrows(NotAllowReceiveRequestException::class.java){
            hrDepartment.receiveRequest(CertificateRequest(1, CertificateType.LABOUR_BOOK))
        }
    }

    @Test
    fun `processNextRequest positive`() {
        mockkStatic(LocalDateTime::class)
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 1L
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY;
        hrDepartment.receiveRequest(CertificateRequest(1, CertificateType.LABOUR_BOOK))

        assertDoesNotThrow{
            hrDepartment.processNextRequest(1)
        }
    }
}