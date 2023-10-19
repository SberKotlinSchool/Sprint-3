package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.lang.reflect.Field
import java.time.Clock
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

internal class HrDepartmentTest {

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun receiveRequestForNdfl(day: DayOfWeek) {
        //given
        val incomeBox = LinkedList<CertificateRequest>()
        val request = CertificateRequest(1L, CertificateType.NDFL)
        incomeBox.push(request)
        val expectedResult = incomeBox.first
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns day

        //then
        assertDoesNotThrow { HrDepartment.receiveRequest(request) }
        val actualResult = getPrivateFields("incomeBox").first
        assertEquals(expectedResult, actualResult)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun receiveRequestForLabourBook(day: DayOfWeek) {
        //given
        val incomeBox = LinkedList<CertificateRequest>()
        val request = CertificateRequest(1L, CertificateType.LABOUR_BOOK)
        incomeBox.push(request)
        val expectedResult = incomeBox.first
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns day

        //then
        assertDoesNotThrow { HrDepartment.receiveRequest(request) }
        val actualResult = getPrivateFields("incomeBox").first
        assertEquals(expectedResult, actualResult)
    }

    @ParameterizedTest
    @EnumSource(value = CertificateType::class, names = ["NDFL", "LABOUR_BOOK"])
    fun processNextRequest(certificateType: CertificateType) {
        //given
        val certificateRequest = mockkClass(CertificateRequest::class)
        val hrEmployeeNumber = 1L
        val data = byteArrayOf(1)
        val expectedResult = Certificate(certificateRequest, 1L, data)
        val incomeBox = getPrivateFields("incomeBox") as LinkedList<CertificateRequest>
        incomeBox.clear()
        incomeBox.push(certificateRequest)
        every { certificateRequest.process(hrEmployeeNumber) } returns expectedResult

        //then
        assertDoesNotThrow { HrDepartment.processNextRequest(hrEmployeeNumber) }
        verify(exactly = 1) { certificateRequest.process(hrEmployeeNumber) }
        val actualResult = getPrivateFields("outcomeOutcome").first
        assertEquals(expectedResult, actualResult)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SATURDAY", "SUNDAY"])
    fun throwWeekendDayException(day: DayOfWeek) {
        //given
        val expectedMessage = "Заказ справков в выходной день не работает"
        val employeeNumber = 1L
        val request = CertificateRequest(employeeNumber, CertificateType.NDFL)
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns day

        //then
        val exception = assertThrows(WeekendDayException::class.java) { HrDepartment.receiveRequest(request) }
        assertEquals(expectedMessage, exception.message)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun throwNotAllowReceiveRequestExceptionForLabourBook(day: DayOfWeek) {
        //given
        val expectedMessage = "Не разрешено принять запрос на справку"
        val request = CertificateRequest(1L, CertificateType.LABOUR_BOOK)
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns day

        //then
        val exception = assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(request) }
        assertEquals(expectedMessage, exception.message)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun throwNotAllowReceiveRequestExceptionForNDFL(day: DayOfWeek) {
        //given
        val expectedMessage = "Не разрешено принять запрос на справку"
        val request = CertificateRequest(1L, CertificateType.NDFL)
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns day

        //then
        val exception = assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(request) }
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun getAndSetClock() {
        //given
        val baseClock = Clock.systemUTC()
        val expectedClock = Clock.offset(baseClock, Duration.ofHours(2))

        //when
        val getClock = HrDepartment.clock
        HrDepartment.clock = Clock.offset(getClock, Duration.ofHours(2))

        //then
        assertEquals(baseClock, getClock)
        assertEquals(expectedClock, HrDepartment.clock)
    }

    private fun getPrivateFields(nameField: String): LinkedList<*> {
        val field: Field = HrDepartment::class.java.getDeclaredField(nameField)
        field.isAccessible = true

        return field.get(HrDepartment) as LinkedList<*>
    }
}