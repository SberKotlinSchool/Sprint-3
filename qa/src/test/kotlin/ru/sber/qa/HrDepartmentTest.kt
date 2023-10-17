package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.stream.Stream
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(MockKExtension::class)
class HrDepartmentTest {
    companion object {
        private val mockClock = mockk<Clock>()
        private val certificateRequestMock = mockk<CertificateRequest>()

        @JvmStatic
        @AfterAll
        fun afterTest() {
            unmockkAll()
        }

        private class NotAllowReceiveRequestExceptionTestDataProvider: ArgumentsProvider {
            override fun provideArguments(p0: ExtensionContext?): Stream<out Arguments> = Stream.of(
                Arguments.of(CertificateType.NDFL, DayOfWeek.MONDAY),
                Arguments.of(CertificateType.NDFL, DayOfWeek.WEDNESDAY),
                Arguments.of(CertificateType.NDFL, DayOfWeek.FRIDAY),
                Arguments.of(CertificateType.LABOUR_BOOK, DayOfWeek.TUESDAY),
                Arguments.of(CertificateType.LABOUR_BOOK, DayOfWeek.THURSDAY)
            )
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SUNDAY", "SATURDAY"])
    fun testReceiveRequestWeekendException(dayOfWeek: DayOfWeek) {
        prepareDate(dayOfWeek)
        assertFailsWith<WeekendDayException> {
            HrDepartment.receiveRequest(CertificateRequest(1, CertificateType.LABOUR_BOOK))
        }
        verify { LocalDateTime.now(mockClock) }
    }

    private fun prepareDate(dayOfWeek: DayOfWeek) {
        val mockLocalDateTime = mockk<LocalDateTime>()
        every { mockLocalDateTime.dayOfWeek } returns dayOfWeek
        mockkStatic(Clock::class)
        every { Clock.systemUTC() } returns mockClock
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(mockClock) } returns mockLocalDateTime
    }

    @ParameterizedTest
    @ArgumentsSource(NotAllowReceiveRequestExceptionTestDataProvider::class)
    fun testReceiveRequestSuccess(certificateType: CertificateType, dayOfWeek: DayOfWeek) {
        prepareDate(dayOfWeek)
        assertDoesNotThrow { HrDepartment.receiveRequest(CertificateRequest(1, certificateType)) }
        verify { LocalDateTime.now(mockClock) }
    }

    @Test
    fun testReceiveRequestNotAllowReceiveRequestException() {
        prepareDate(DayOfWeek.TUESDAY)
        every { certificateRequestMock.certificateType } returns CertificateType.NDFL
        assertFailsWith<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequestMock)
        }
        verify { LocalDateTime.now(mockClock) }
    }

    @Test
    fun testProcessNextRequest() {
        prepareDate(DayOfWeek.MONDAY)
        every { certificateRequestMock.certificateType }.returns(CertificateType.NDFL)
        every { certificateRequestMock.process(1) } returns Certificate(
            certificateRequestMock, 1, byteArrayOf()
        )
        HrDepartment.receiveRequest(certificateRequestMock)
        HrDepartment.processNextRequest(1)
        verify { certificateRequestMock.process(1) }
    }
}