package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.LinkedList
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

private val moscowZoneId = ZoneId.of("Europe/Moscow")

class HrDepartmentTest {

    enum class WeekDays(val dateTime: ZonedDateTime) {
        Monday(LocalDate.of(2023, 10, 2).atStartOfDay(moscowZoneId)),
        Tuesday(LocalDate.of(2023, 10, 3).atStartOfDay(moscowZoneId)),
        Wednesday(LocalDate.of(2023, 10, 4).atStartOfDay(moscowZoneId)),
        Thursday(LocalDate.of(2023, 10, 5).atStartOfDay(moscowZoneId)),
        Friday(LocalDate.of(2023, 10, 6).atStartOfDay(moscowZoneId)),
        Saturday(LocalDate.of(2023, 10, 7).atStartOfDay(moscowZoneId)),
        Sunday(LocalDate.of(2023, 10, 8).atStartOfDay(moscowZoneId))
    }



    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(100)
    }

    @ParameterizedTest
    @EnumSource(WeekDays::class, names = ["Saturday", "Sunday"])
    fun `ensure hr department doesn't work on weekend`(weekDays: WeekDays) {
        val zonedDateTime = weekDays.dateTime
        val hrDepartment = HrDepartment.apply {
            clock = Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.zone)
        }

        assertThrows(WeekendDayException::class.java) {
            hrDepartment.receiveRequest(CertificateRequest(1L, CertificateType.LABOUR_BOOK))
        }
    }

    @ParameterizedTest
    @EnumSource(WeekDays::class, names = ["Monday", "Wednesday", "Friday"])
    fun `ensure labour book couldn't be prepared on odd week days`(weekDays: WeekDays) {
        val zonedDateTime = weekDays.dateTime
        val hrDepartment = HrDepartment.apply {
            clock = Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.zone)
        }

        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(CertificateRequest(1L, CertificateType.LABOUR_BOOK))
        }
    }

    @ParameterizedTest
    @EnumSource(WeekDays::class, names = ["Tuesday", "Thursday"])
    fun `ensure labour book could be prepared on even week days`(weekDays: WeekDays) {
        val zonedDateTime = weekDays.dateTime
        val hrDepartment = HrDepartment.apply {
            clock = Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.zone)
        }

        assertDoesNotThrow {
            hrDepartment.receiveRequest(CertificateRequest(1L, CertificateType.LABOUR_BOOK))
        }
    }

    @ParameterizedTest
    @EnumSource(WeekDays::class, names = ["Tuesday", "Thursday"])
    fun `ensure ndfl couldn't be prepared on even week days`(weekDays: WeekDays) {
        val zonedDateTime = weekDays.dateTime
        val hrDepartment = HrDepartment.apply {
            clock = Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.zone)
        }

        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(CertificateRequest(1L, CertificateType.NDFL))
        }
    }

    @ParameterizedTest
    @EnumSource(WeekDays::class, names = ["Monday", "Wednesday", "Friday"])
    fun `ensure ndfl could be prepared on odd week days`(weekDays: WeekDays) {
        val zonedDateTime = weekDays.dateTime
        val hrDepartment = HrDepartment.apply {
            clock = Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.zone)
        }

        assertDoesNotThrow {
            hrDepartment.receiveRequest(CertificateRequest(1L, CertificateType.NDFL))
        }
    }

    @Test
    fun `ensure request is processed after receiving it`() {
        val employeeNumber = 1L
        val zonedDateTime = WeekDays.Monday.dateTime
        val hrDepartment = HrDepartment.apply {
            clock = Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.zone)
        }
        val certificateRequests = HrDepartment::class.declaredMemberProperties.first { it.name == "incomeBox" }
            .also { it.isAccessible = true }
            .getter.call() as LinkedList<CertificateRequest>
        certificateRequests += CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)

        assertDoesNotThrow {
            hrDepartment.processNextRequest(employeeNumber)
        }
    }

    @Test
    fun `ensure could be processed only after receiving it`() {
        val zonedDateTime = WeekDays.Monday.dateTime
        val hrDepartment = HrDepartment.apply {
            clock = Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.zone)
        }

        assertThrows(NullPointerException::class.java) {
            hrDepartment.processNextRequest(1L)
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        resetInternalState()
    }

    private fun resetInternalState() {
        HrDepartment::class.declaredMemberProperties
            .filter { it.visibility == KVisibility.PRIVATE }
            .forEach {
                it.isAccessible = true
                val linkedList = it.getter.call() as LinkedList<*>
                linkedList.clear()
            }
    }
}