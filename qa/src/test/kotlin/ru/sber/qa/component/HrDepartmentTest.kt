package ru.sber.qa.component


import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import ru.sber.qa.*
import ru.sber.qa.model.Certificate
import ru.sber.qa.model.CertificateRequest
import ru.sber.qa.model.CertificateType
import java.time.*
import java.time.DayOfWeek.*
import java.util.*
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HrDepartmentTest {

    private val basedClock = Clock.fixed(Instant.parse("2022-10-24T12:00:00Z"), ZoneId.systemDefault()) // monday
    private val hrDepartment = HrDepartment

    @AfterEach
    fun clear() {
        clearLinkedListField<LinkedList<CertificateRequest>>(hrDepartment, INCOME_BOX_FIELD_NAME)
        clearLinkedListField<LinkedList<Certificate>>(hrDepartment, OUTCOME_BOX_FIELD_NAME)
    }

    @ParameterizedTest(name = "#{index} - {0}")
    @EnumSource(value = DayOfWeek::class, names = ["SUNDAY", "SATURDAY"])
    @DisplayName("receiveRequest - WeekendDayException")
    fun receiveRequest_WeekendDayException(day: DayOfWeek) {
        val clock = getClock(day)
        hrDepartment.clock = clock

        assertFailsWith<Exception> { hrDepartment.receiveRequest(CertificateRequest(1L, CertificateType.NDFL)) }
    }

    @ParameterizedTest(name = "#{index} - day {0}, type {1}")
    @MethodSource("receiveRequestSuccessTestArguments")
    @DisplayName("receiveRequest - success")
    fun receiveRequest_success(day: DayOfWeek, certType: CertificateType) {
        val clock = getClock(day)
        hrDepartment.clock = clock
        val emplNumber = 7L

        hrDepartment.receiveRequest(CertificateRequest(emplNumber, certType))

        val incomeBox = getField<LinkedList<CertificateRequest>>(hrDepartment, INCOME_BOX_FIELD_NAME)
        assertNotNull(incomeBox)
        assertEquals(1, incomeBox.size)
        val actual = incomeBox[0]
        assertEquals(certType, actual.certificateType)
        assertEquals(emplNumber, actual.employeeNumber)
    }

     private fun receiveRequestSuccessTestArguments(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(MONDAY, CertificateType.NDFL),
            Arguments.of(WEDNESDAY, CertificateType.NDFL),
            Arguments.of(FRIDAY, CertificateType.NDFL),
            Arguments.of(THURSDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(THURSDAY, CertificateType.LABOUR_BOOK),
        )
    }

    @ParameterizedTest(name = "#{index} - day {0}, type {1}")
    @MethodSource("receiveRequestNotAllowReceiveRequestExceptionTestArguments")
    @DisplayName("receiveRequest - NotAllowReceiveRequestException")
    fun receiveRequest_NotAllowReceiveRequestException(day: DayOfWeek, certType: CertificateType) {
        val clock = getClock(day)
        hrDepartment.clock = clock

        assertFailsWith<Exception> { hrDepartment.receiveRequest(CertificateRequest(1L, certType)) }
    }

    private fun receiveRequestNotAllowReceiveRequestExceptionTestArguments(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(MONDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(WEDNESDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(FRIDAY, CertificateType.LABOUR_BOOK),
            Arguments.of(THURSDAY, CertificateType.NDFL),
            Arguments.of(THURSDAY, CertificateType.NDFL),
        )
    }

    @Test
    @DisplayName("processNextRequest Test")
    fun processNextRequestTest() {
        val clock = getClock(MONDAY)
        hrDepartment.clock = clock
        val queueSize = 5
        val testByteArray = getTestByteArray()

        // подготовка очереди из 5 mockRequest
        val incomeBoxValue: LinkedList<CertificateRequest> = LinkedList()

        for (emplNumber in 1..queueSize) {
            val mockkRequest = mockkClass(CertificateRequest::class)
            val hrNumber = (emplNumber + queueSize).toLong()
            val expectedCertificate = Certificate(
                CertificateRequest(emplNumber.toLong(), CertificateType.NDFL), hrNumber, testByteArray
            )
            every { mockkRequest.process(hrNumber) } returns expectedCertificate
            incomeBoxValue.add(mockkRequest)
        }
        addAllToLinkedListField(hrDepartment, INCOME_BOX_FIELD_NAME, incomeBoxValue)

        // проверка обработки очереди
        for (emplNumber in 1..queueSize) {
            val hrNumber = (emplNumber + queueSize).toLong()

            // проеряемый метод
            hrDepartment.processNextRequest(hrNumber)

            val incomeBox = getField<LinkedList<CertificateRequest>>(hrDepartment, INCOME_BOX_FIELD_NAME)
            assertEquals(queueSize-emplNumber, incomeBox.size)
            val outcomeBox = getField<LinkedList<Certificate>>(hrDepartment, OUTCOME_BOX_FIELD_NAME)
            assertEquals(emplNumber, outcomeBox.size)
            assertEquals(hrNumber, outcomeBox[0].processedBy)
            assertEquals(emplNumber.toLong(), outcomeBox[0].certificateRequest.employeeNumber)
            assertEquals(CertificateType.NDFL, outcomeBox[0].certificateRequest.certificateType)
            assertEquals(testByteArray, outcomeBox[0].data)
        }
    }

    private fun getClock(day: DayOfWeek): Clock? {
        return Clock.offset(basedClock, Duration.ofDays((day.value - 1).toLong()))
    }
}