package test

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.sber.qa.*
import java.lang.reflect.*
import java.time.*
import java.util.*
import kotlin.random.Random

internal class HrDepartmentTest{
    private val certificateRequest = mockkClass(CertificateRequest::class)
    private val certificate = mockkClass(Certificate::class)

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    companion object {
        @JvmStatic
        fun receiveRequestWeekendDayExceptionParams() = listOf(
            Arguments.of(DayOfWeek.SATURDAY),
            Arguments.of(DayOfWeek.SUNDAY)
        )

        @JvmStatic
        fun receiveRequestNotAllowReceiveRequestExceptionParams() = listOf(
            Arguments.of(DayOfWeek.TUESDAY, CertificateType.NDFL),
            Arguments.of(DayOfWeek.THURSDAY, CertificateType.NDFL),
            Arguments.of(DayOfWeek.MONDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(DayOfWeek.WEDNESDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(DayOfWeek.FRIDAY, CertificateType.LABOUR_BOOK)
        )

        @JvmStatic
        fun receiveRequestParams() = listOf(
            Arguments.of(DayOfWeek.TUESDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(DayOfWeek.THURSDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(DayOfWeek.MONDAY, CertificateType.NDFL),
            Arguments.of(DayOfWeek.WEDNESDAY, CertificateType.NDFL),
            Arguments.of(DayOfWeek.FRIDAY, CertificateType.NDFL)
        )

        @JvmStatic
        fun processNextRequestParams() = listOf(
            Arguments.of(DayOfWeek.TUESDAY, CertificateType.LABOUR_BOOK, Random.nextLong(-1000L,1000L)),
            Arguments.of(DayOfWeek.THURSDAY, CertificateType.LABOUR_BOOK, Random.nextLong(-1000L,1000L)),
            Arguments.of(DayOfWeek.MONDAY, CertificateType.NDFL, Random.nextLong(-1000L,1000L)),
            Arguments.of(DayOfWeek.WEDNESDAY, CertificateType.NDFL, Random.nextLong(-1000L,1000L)),
            Arguments.of(DayOfWeek.FRIDAY, CertificateType.NDFL, 0L)
        )
    }

    @ParameterizedTest
    @MethodSource("receiveRequestWeekendDayExceptionParams")
    fun testReceiveRequestWeekendDayException(currentDay: DayOfWeek) {
        // given
        every { LocalDateTime.now(Clock.systemUTC()).dayOfWeek } returns currentDay
        // result
        assertThrows(WeekendDayException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @MethodSource("receiveRequestNotAllowReceiveRequestExceptionParams")
    fun testReceiveRequestNotAllowReceiveRequestException(currentDay: DayOfWeek, certificateType: CertificateType) {
        // given
        every { LocalDateTime.now(Clock.systemUTC()).dayOfWeek } returns currentDay
        every { certificateRequest.certificateType } returns certificateType
        // result
        assertThrows(NotAllowReceiveRequestException::class.java) { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @MethodSource("receiveRequestParams")
    fun testReceiveRequest(currentDay: DayOfWeek, certificateType: CertificateType) {
        // given
        every { LocalDateTime.now(Clock.systemUTC()).dayOfWeek } returns currentDay
        every { certificateRequest.certificateType } returns certificateType
        // result
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @MethodSource("processNextRequestParams")
    fun testProcessNextRequest(currentDay: DayOfWeek, certificateType: CertificateType, hrEmployeeNumber: Long) {
        // given
        val incomeBoxForReflection: LinkedList<CertificateRequest> = LinkedList()
        val outcomeOutcomeForReflection: LinkedList<Certificate> = LinkedList()
        val incomeBox: LinkedList<CertificateRequest>
        val outcomeOutcome: LinkedList<Certificate>

        every { LocalDateTime.now(Clock.systemUTC()).dayOfWeek } returns currentDay
        every { certificateRequest.certificateType } returns certificateType
        every { certificateRequest.process(hrEmployeeNumber) } returns certificate
        // when
        HrDepartment::class.java.getDeclaredField("incomeBox").let {field ->
            field.isAccessible = true
            incomeBox = field.get(incomeBoxForReflection) as LinkedList<CertificateRequest>
        }

        incomeBox.push(certificateRequest)

        HrDepartment::class.java.getDeclaredField("outcomeOutcome").let {field ->
            field.isAccessible = true
            outcomeOutcome = field.get(outcomeOutcomeForReflection) as LinkedList<Certificate>
        }
        // result
        assertDoesNotThrow { HrDepartment.processNextRequest(hrEmployeeNumber) }
        assertEquals(certificate, outcomeOutcome[0])
    }

    @Test
    fun testProcessNextRequestToThrowNullPointerException(){
        //result
        assertThrows(NullPointerException::class.java) { HrDepartment.processNextRequest(2L) }
    }
}