package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.*
import java.util.LinkedList

class HrDepartmentTest {
    private val certificateRequest = mockk<CertificateRequest>()
    private lateinit var currentDayOfWeek: MockKStubScope<DayOfWeek, DayOfWeek>

    @BeforeEach
    fun init() {
        mockkStatic(LocalDateTime::class)
        currentDayOfWeek = every { LocalDateTime.now(any<Clock>()).dayOfWeek }
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SUNDAY", "SATURDAY"])
    fun `throw WeekendDayException`(dayOfWeek: DayOfWeek) {
        // Given
        currentDayOfWeek returns dayOfWeek
        // When
        val res = kotlin.runCatching { HrDepartment.receiveRequest(certificateRequest) }
        // Then
        assertThrows(
            WeekendDayException::class.java,
            { res.getOrThrow() },
            "Заказ справков в выходной день не работает"
        )
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun `throw NotAllowReceiveRequestException if CertificateType is NDFL`(dayOfWeek: DayOfWeek) {
        `throw NotAllowReceiveRequestException`(dayOfWeek, CertificateType.NDFL)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun `throw NotAllowReceiveRequestException if CertificateType is LABOUR_BOOK`(dayOfWeek: DayOfWeek) {
        `throw NotAllowReceiveRequestException`(dayOfWeek, CertificateType.LABOUR_BOOK)
    }

    private fun `throw NotAllowReceiveRequestException`(dayOfWeek: DayOfWeek, cert: CertificateType) {
        // Given
        every { certificateRequest.certificateType } returns cert
        currentDayOfWeek returns dayOfWeek
        // When
        val res = kotlin.runCatching { HrDepartment.receiveRequest(certificateRequest) }
        // Then
        assertThrows(
            NotAllowReceiveRequestException::class.java,
            { res.getOrThrow() },
            "Не разрешено принять запрос на справку"
        )
    }

    @Test
    fun `push in incomeBox`() {
        // Given
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        currentDayOfWeek returns DayOfWeek.MONDAY
        // When
        HrDepartment.receiveRequest(certificateRequest)
        // Then
        val incomeBox = incomeBoxRef()
        assertTrue { incomeBox.isNotEmpty() }
        assertTrue { incomeBox.size == 1 }
    }

    @Test
    fun `push in outcomeOutcome`() {
        // Given
        val cert = mockk<Certificate>()
        every { certificateRequest.process(any()) } returns cert
        incomeBoxRef().push(certificateRequest)
        // When
        HrDepartment.processNextRequest(1)
        // Then
        val outcomeOutcome = HrDepartment.getPrivateFiled<LinkedList<CertificateRequest>>("outcomeOutcome")
        assertTrue { outcomeOutcome.isNotEmpty() }
        assertTrue { outcomeOutcome.size == 1 }
    }

    private fun incomeBoxRef() = HrDepartment.getPrivateFiled<LinkedList<CertificateRequest>>("incomeBox")
    private fun <T> Any.getPrivateFiled(fieldName: String): T {
        val field = this.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(this) as T
    }
}