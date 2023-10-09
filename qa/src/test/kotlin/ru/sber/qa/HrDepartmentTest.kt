package ru.sber.qa

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
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

class HrDepartmentTest {

    @MockK(relaxed = true)
    lateinit var certificateRequest: CertificateRequest

    companion object {
        @JvmStatic
        fun negativeSequence() = get()

        private fun get(one: Int = 1): Stream<Arguments> =
            Arrays.stream(DayOfWeek.values()).filter { it.ordinal < 5 }
                .map { Arguments.of(
                        it,
                        if ((it.ordinal % 2) == one) CertificateType.NDFL
                        else CertificateType.LABOUR_BOOK
                    )
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
    fun `WeekendDayException check`(dayOfWeek: DayOfWeek) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek

        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @MethodSource("negativeSequence")
    fun `NotAllowReceiveRequestException check`(
        dayOfWeek: DayOfWeek, certificateType: CertificateType
    ) {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns certificateType

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `NullPointerException check`() {
        assertThrows<NullPointerException> { HrDepartment.processNextRequest(1L) }
    }
}