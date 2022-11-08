package ru.sber.qa

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream

internal class HrDepartmentTest {
    @MockK(relaxed = true)
    lateinit var certificateRequest: CertificateRequest

    private val hrNumber = 1L

    companion object {
        @JvmStatic
        fun positiveTypeAndDaySequence(): Stream<Arguments> = Arrays.stream(DayOfWeek.values())
            .filter { it.ordinal < 5 }
            .map {
                Arguments.of(it, if (it.ordinal % 2 == 0) CertificateType.NDFL else CertificateType.LABOUR_BOOK)
            }

        @JvmStatic
        fun negativeTypeAndDaySequence(): Stream<Arguments> = Arrays.stream(DayOfWeek.values())
            .filter { it.ordinal < 5 }
            .map {
                Arguments.of(it, if (it.ordinal % 2 == 1) CertificateType.NDFL else CertificateType.LABOUR_BOOK)
            }
    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic("java.time.LocalDateTime")
        mockkObject(Scanner)

        every { Scanner.getScanData() } returns byteArrayOf(1)
    }

    @AfterEach
    fun tearDown() = clearAllMocks()


    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SATURDAY", "SUNDAY"])
    fun `given weekend days when receiveRequest then WeekendDayException thrown`(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek

        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @MethodSource("negativeTypeAndDaySequence")
    fun `given incorrect CertificateType for dayOfWeek when receiveRequest and processNextRequest then NotAllowReceiveRequestException thrown`(
        dayOfWeek: DayOfWeek, certificateType: CertificateType
    ) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns certificateType

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @MethodSource("positiveTypeAndDaySequence")
    fun `given correct CertificateType for dayOfWeek when receiveRequest and processNextRequest then no Exceptions thrown`(
        dayOfWeek: DayOfWeek, certificateType: CertificateType
    ) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns certificateType

        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        assertDoesNotThrow { HrDepartment.processNextRequest(hrNumber) }
    }

    @Test
    fun `given request not received when processNextRequest then NullPointerException thrown`() {
        assertThrows<NullPointerException> { HrDepartment.processNextRequest(1L) }
    }
}
