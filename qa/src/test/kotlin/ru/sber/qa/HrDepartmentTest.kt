package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.lang.reflect.Field
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals

class HrDepartmentTest {

    private val employeeNumber: Long = 1111L
    private val certificateNdflRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
    private val certificateLabourRequest = CertificateRequest(employeeNumber + 1, CertificateType.LABOUR_BOOK)

    @BeforeEach
    fun setUp() {
        mockkObject(HrDepartment)
        mockkObject(Scanner)
        mockkStatic(LocalDateTime::class)
    }

    @AfterEach
    fun closeUp() {
        unmockkAll()
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SUNDAY", "SATURDAY"])
    fun shouldThrowWeekendDayException(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek

        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateNdflRequest)
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun shouldNdflCertificateAllowReceiveRequest(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek

        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateNdflRequest)
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun shouldLabourCertificateAllowReceiveRequest(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek

        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateLabourRequest)
        }
    }

    @Test
    fun shouldThrowNotAllowReceiveRequestException() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateLabourRequest)
        }
    }

    @Test
    fun shouldPushRequestIntoIncomeBox() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        HrDepartment.receiveRequest(certificateLabourRequest)

        val incomeBox: LinkedList<CertificateRequest> = getPrivateFieldFromClass("incomeBox")

        assertEquals(1, incomeBox.size)
    }

    @Test
    fun shouldNotBeEmptyInOutcome() {
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 1000L

        val incomeBox: LinkedList<CertificateRequest> = getPrivateFieldFromClass("incomeBox")
        incomeBox.add(certificateLabourRequest)
        val outcome: LinkedList<Certificate> = getPrivateFieldFromClass("outcomeOutcome")

        HrDepartment.processNextRequest(employeeNumber)

        assertEquals(1, outcome.size)
    }

    private fun <Type> getPrivateFieldFromClass(fieldName: String): Type {
        val field: Field = HrDepartment::class.java.getDeclaredField(fieldName)
        field.isAccessible = true

        return field.get(HrDepartment) as Type
    }

}