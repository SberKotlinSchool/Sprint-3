package ru.sber.qa

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime

internal class HrDepartmentTest {
    @MockK
    lateinit var certificateRequest: CertificateRequest

    private val hrNumber = 1L

//    companion object {
//        @JvmStatic
//        fun positiveTypeAndDaySequence(): List<Arguments> = DayOfWeek.values().mapIndexed { idx, day ->
//            Arguments.of(
//                day, if (idx % 2 == 0) CertificateType.NDFL else CertificateType.LABOUR_BOOK
//            )
//        }
//    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic("java.time.LocalDateTime")

        every { certificateRequest.process(hrNumber) } returns Certificate(certificateRequest, hrNumber, byteArrayOf(1))
    }

    @AfterEach
    fun tearDown() = clearAllMocks()


    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SATURDAY", "SUNDAY"])
    fun `receiveRequest when Weekend throws WeekendDayException`(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek

        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun `receiveRequest when CertificateType NDFL throws NotAllowReceiveRequestException`(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun `receiveRequest when CertificateType LABOUR_BOOK throws NotAllowReceiveRequestException`(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun `receiveRequest and processNextRequest when CertificateType NDFL not throw any Exception`(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        assertDoesNotThrow { HrDepartment.processNextRequest(hrNumber) }
    }

    @ParameterizedTest
//    @MethodSource("ru.sber.qa.HrDepartmentTest#positiveTypeAndDaySequence")
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun `receiveRequest and processNextRequest when CertificateType LABOUR_BOOK not throw any Exception`(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        assertDoesNotThrow { HrDepartment.processNextRequest(hrNumber) }
    }

    @Test
    fun `processNextRequest when not received request throw NullPointerException`() {
        assertThrows<NullPointerException> { HrDepartment.processNextRequest(1L) }
    }


}