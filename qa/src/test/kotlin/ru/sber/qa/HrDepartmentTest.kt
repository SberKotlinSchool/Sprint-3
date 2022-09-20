package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    @BeforeEach
    internal fun setUp() {
        mockkStatic(LocalDateTime::class)
    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }

    companion object {
        @JvmStatic
        fun getReceiveRequestDataWithException() = listOf(
            Arguments.of(DayOfWeek.SUNDAY, CertificateType.NDFL, WeekendDayException::class),
            Arguments.of(DayOfWeek.SATURDAY, CertificateType.NDFL, WeekendDayException::class),
            Arguments.of(DayOfWeek.MONDAY, CertificateType.LABOUR_BOOK, NotAllowReceiveRequestException::class),
            Arguments.of(DayOfWeek.WEDNESDAY, CertificateType.LABOUR_BOOK, NotAllowReceiveRequestException::class),
            Arguments.of(DayOfWeek.FRIDAY, CertificateType.LABOUR_BOOK, NotAllowReceiveRequestException::class),
            Arguments.of(DayOfWeek.TUESDAY, CertificateType.NDFL, NotAllowReceiveRequestException::class),
            Arguments.of(DayOfWeek.THURSDAY, CertificateType.NDFL, NotAllowReceiveRequestException::class),
        )
    }

    @ParameterizedTest
    @MethodSource("getReceiveRequestDataWithException")
    fun `receiveRequest with exception`(
        mockkDay: DayOfWeek, certificateType: CertificateType, exception: KClass<Throwable>
    ) {

        every { LocalDateTime.now(Clock.systemUTC()).dayOfWeek } returns mockkDay

        val certificateRequest = CertificateRequest(1L, certificateType)

        assertFailsWith(
            exceptionClass = exception,
            block =
            {
                HrDepartment.receiveRequest(certificateRequest)
            }
        )
    }


    @Test
    fun receiveRequest() {

        every { LocalDateTime.now(Clock.systemUTC()).dayOfWeek } returns DayOfWeek.MONDAY

        val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)

        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    private fun <T> getField(obj: Any, fieldName: String): T {

        val field = obj.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true

        return field.get(obj) as T
    }

    @Test
    fun processNextRequest() {

        val incomeBox = getField<LinkedList<CertificateRequest>>(HrDepartment, "incomeBox")
        val outcomeOutcome = getField<LinkedList<CertificateRequest>>(HrDepartment, "outcomeOutcome")

        val certificate = mockk<Certificate>()
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.process(1L) }.returns(certificate)

        incomeBox.push(certificateRequest);

        HrDepartment.processNextRequest(1L)

        assertEquals(1, outcomeOutcome.size)
    }

}