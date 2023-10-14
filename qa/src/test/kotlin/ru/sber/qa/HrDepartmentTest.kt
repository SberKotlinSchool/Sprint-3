package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.lang.reflect.Field
import java.time.*
import java.util.*

internal class HrDepartmentTest {

    private lateinit var currentDayOfWeek: MockKStubScope<DayOfWeek, DayOfWeek>
    private val certificateRequest = mockk<CertificateRequest>()
    private val data = byteArrayOf(1)
    private val certificate = Certificate(certificateRequest, 1L, data)

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
        currentDayOfWeek = every { LocalDateTime.now(any<Clock>()).dayOfWeek }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SUNDAY", "SATURDAY"])
    fun shouldNotReceiveRequestOnWeekend(dayOfWeek: DayOfWeek) {
        currentDayOfWeek returns dayOfWeek
        val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)
        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun shouldNotReceiveNdflOnWrongDays(dayOfWeek: DayOfWeek) {
        currentDayOfWeek returns dayOfWeek
        val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun shouldNotReceiveLabourOnWrongDays(dayOfWeek: DayOfWeek) {
        currentDayOfWeek returns dayOfWeek
        val certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun shouldReceiveNdflOnCorrectDays(dayOfWeek: DayOfWeek) {
        currentDayOfWeek returns dayOfWeek
        val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)
        val incomeBox = getPrivateFields("incomeBox")
        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }
        assertEquals(certificateRequest, incomeBox.first)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun shouldReceiveLabourOnCorrectDays(dayOfWeek: DayOfWeek) {
        currentDayOfWeek returns dayOfWeek
        val certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)
        val incomeBox = getPrivateFields("incomeBox")
        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }
        assertEquals(certificateRequest, incomeBox.first)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun shouldProcessNextRequestForNdfl(dayOfWeek: DayOfWeek) {
        currentDayOfWeek returns dayOfWeek
        every { certificateRequest.process(1L) } returns certificate
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        HrDepartment.receiveRequest(certificateRequest)
        val outcomeOutcome = getPrivateFields("outcomeOutcome")
        HrDepartment.processNextRequest(1L)
        verify(exactly = 1) { certificateRequest.process(any()) }
        assertEquals(certificate, outcomeOutcome.first)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun shouldProcessNextRequestForLabour(dayOfWeek: DayOfWeek) {
        currentDayOfWeek returns dayOfWeek
        every { certificateRequest.process(1L) } returns certificate
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        HrDepartment.receiveRequest(certificateRequest)
        val outcomeOutcome = getPrivateFields("outcomeOutcome")
        HrDepartment.processNextRequest(1L)
        verify(exactly = 1) { certificateRequest.process(any()) }
        assertEquals(certificate, outcomeOutcome.first)
    }

    private fun getPrivateFields(nameField: String): LinkedList<*> {
        val field: Field = HrDepartment::class.java.getDeclaredField(nameField)
        field.isAccessible = true

        return field.get(HrDepartment) as LinkedList<*>
    }
}