package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import ru.sber.qa.HrDepartment.processNextRequest
import ru.sber.qa.HrDepartment.receiveRequest
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.stream.Stream

internal class HrDepartmentTest {

    private val clock: Clock = Clock.systemUTC()
    private val hrEmployeeNumber = 1L

    @BeforeEach
    fun beforeEach() = mockkStatic(LocalDateTime::class)

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SUNDAY", "SATURDAY"])
    fun `receiveRequest() throws WeekendDayException`(dayOfWeek: DayOfWeek) {
        val certificateRequest = mockk<CertificateRequest>()
        every { LocalDateTime.now(clock).dayOfWeek } returns dayOfWeek

        assertThrows<WeekendDayException> {
            receiveRequest(certificateRequest)
        }
    }

    @ParameterizedTest
    @MethodSource("notAllowed")
    fun `receiveRequest() throws NotAllowReceiveRequestException`(certificateType: CertificateType, dayOfWeek: DayOfWeek) {
        val certificateRequest = CertificateRequest(hrEmployeeNumber, certificateType)
        every { LocalDateTime.now(clock).dayOfWeek } returns dayOfWeek

        assertThrows<NotAllowReceiveRequestException> {
            receiveRequest(certificateRequest)
        }
    }

    @ParameterizedTest
    @MethodSource("allowed")
    fun `receiveRequest() success test`(certificateType: CertificateType, dayOfWeek: DayOfWeek) {
        val certificateRequest = CertificateRequest(hrEmployeeNumber, certificateType)
        every { LocalDateTime.now(clock).dayOfWeek } returns dayOfWeek

        assertDoesNotThrow {
            receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `processNextRequest() test`() {

        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.LABOUR_BOOK)

        mockkObject(certificateRequest)
        every { LocalDateTime.now(clock).dayOfWeek } returns DayOfWeek.TUESDAY

        every { certificateRequest.process(hrEmployeeNumber) } returns Certificate(
            certificateRequest, hrEmployeeNumber,
            byteArrayOf(100)
        )
        receiveRequest(certificateRequest)
        assertDoesNotThrow { processNextRequest(hrEmployeeNumber) }
        verify { certificateRequest.process(hrEmployeeNumber) }

        unmockkObject(certificateRequest)
    }

    @AfterEach
    fun afterEach() = unmockkStatic(LocalDateTime::class)

    companion object {
        @JvmStatic
        fun allowed(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(CertificateType.NDFL, DayOfWeek.MONDAY),
                Arguments.of(CertificateType.NDFL, DayOfWeek.WEDNESDAY),
                Arguments.of(CertificateType.NDFL, DayOfWeek.FRIDAY),
                Arguments.of(CertificateType.LABOUR_BOOK , DayOfWeek.TUESDAY),
                Arguments.of(CertificateType.LABOUR_BOOK , DayOfWeek.THURSDAY),
            )
        }

        @JvmStatic
        fun notAllowed(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(CertificateType.LABOUR_BOOK, DayOfWeek.MONDAY),
                Arguments.of(CertificateType.LABOUR_BOOK, DayOfWeek.WEDNESDAY),
                Arguments.of(CertificateType.LABOUR_BOOK, DayOfWeek.FRIDAY),
                Arguments.of(CertificateType.NDFL , DayOfWeek.TUESDAY),
                Arguments.of(CertificateType.NDFL , DayOfWeek.THURSDAY),
            )
        }
    }
}