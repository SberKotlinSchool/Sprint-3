package ru.sber.qa

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.*
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HrDepartmentTest {

    private val EMPLOYEE_NUMBER = 1L

    private lateinit var hr: HrDepartment

    companion object {
        private const val MONDAY = "2022-09-05T00:00:00Z"
        private const val TUESDAY = "2022-09-06T00:00:00Z"
        private const val WEDNESDAY = "2022-09-07T00:00:00Z"
        private const val THURSDAY = "2022-09-08T00:00:00Z"
        private const val FRIDAY = "2022-09-09T00:00:00Z"
        private const val SATURDAY = "2022-09-10T00:00:00Z"
        private const val SUNDAY = "2022-09-11T00:00:00Z"
    }

    @BeforeEach
    fun init() {
        hr = mockkClass(HrDepartment::class)
    }

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @ParameterizedTest
    @MethodSource("getDataForReceiveRequestSuccess")
    fun receiveRequestSuccess(dateInstant: Instant, certificateType: CertificateType) {
        val request = CertificateRequest(EMPLOYEE_NUMBER, certificateType)
        every { hr.clock } returns Clock.fixed(dateInstant, ZoneOffset.UTC)
        assertDoesNotThrow { HrDepartment.receiveRequest(request) }
    }

    private fun getDataForReceiveRequestSuccess() = Stream.of(
        Arguments.of(Instant.parse(MONDAY), CertificateType.NDFL),
        Arguments.of(Instant.parse(WEDNESDAY), CertificateType.NDFL),
        Arguments.of(Instant.parse(FRIDAY), CertificateType.NDFL),
        Arguments.of(Instant.parse(TUESDAY), CertificateType.LABOUR_BOOK),
        Arguments.of(Instant.parse(THURSDAY), CertificateType.LABOUR_BOOK)
    )

    @ParameterizedTest
    @MethodSource("getDataForReceiveRequestThrow")
    fun receiveRequestThrowNotAllowReceiveRequestException(
        dateInstant: Instant,
        certificateType: CertificateType,
        exception: Class<Exception>
    ) {
        val request = CertificateRequest(EMPLOYEE_NUMBER, certificateType)
        every { hr.clock } returns Clock.fixed(dateInstant, ZoneOffset.UTC)
        assertThrows(exception) { HrDepartment.receiveRequest(request) }
    }

    private fun getDataForReceiveRequestThrow() = Stream.of(
        Arguments.of(Instant.parse(TUESDAY), CertificateType.NDFL, NotAllowReceiveRequestException::class.java),
        Arguments.of(Instant.parse(THURSDAY), CertificateType.NDFL, NotAllowReceiveRequestException::class.java),
        Arguments.of(Instant.parse(SATURDAY), CertificateType.NDFL, WeekendDayException::class.java),
        Arguments.of(Instant.parse(SUNDAY), CertificateType.NDFL, WeekendDayException::class.java),
        Arguments.of(
            Instant.parse(MONDAY),
            CertificateType.LABOUR_BOOK,
            NotAllowReceiveRequestException::class.java
        ),
        Arguments.of(
            Instant.parse(WEDNESDAY),
            CertificateType.LABOUR_BOOK,
            NotAllowReceiveRequestException::class.java
        ),
        Arguments.of(
            Instant.parse(FRIDAY),
            CertificateType.LABOUR_BOOK,
            NotAllowReceiveRequestException::class.java
        ),
        Arguments.of(Instant.parse(SATURDAY), CertificateType.LABOUR_BOOK, WeekendDayException::class.java),
        Arguments.of(Instant.parse(SUNDAY), CertificateType.LABOUR_BOOK, WeekendDayException::class.java)
    )

    @Test
    fun processNextRequest() {
        val certificate = mockkClass(Certificate::class)
        val certificateRequest = mockkClass(CertificateRequest::class)
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { certificateRequest.employeeNumber } returns EMPLOYEE_NUMBER
        every { certificateRequest.process(EMPLOYEE_NUMBER) } returns certificate
        every { hr.clock } returns Clock.fixed(Instant.parse(MONDAY), ZoneOffset.UTC)

        HrDepartment.receiveRequest(certificateRequest)
        HrDepartment.processNextRequest(EMPLOYEE_NUMBER)

        val outcomeOutcomeField = HrDepartment.javaClass.getDeclaredField("outcomeOutcome")
        outcomeOutcomeField.trySetAccessible()
        val outcomeOutcome = outcomeOutcomeField.get(HrDepartment) as LinkedList<*>
        assertNotNull(outcomeOutcome)
        assertFalse { outcomeOutcome.isEmpty() }
        assertEquals(outcomeOutcome.size, 1)
        assertEquals(certificate, outcomeOutcome[0])
    }

}