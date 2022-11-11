package ru.sber.qa

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Arrays
import java.util.LinkedList
import java.util.stream.Stream

internal class HrDepartmentTest {
    @MockK(relaxed = true)
    lateinit var certificateRequest: CertificateRequest

    private val hrNumber = 1L

    companion object {
        @JvmStatic
        fun positiveTypeAndDaySequence() = generateEvenOrOddSequenceOfTypeAndDay(0)

        @JvmStatic
        fun negativeTypeAndDaySequence() = generateEvenOrOddSequenceOfTypeAndDay(1)

        private fun generateEvenOrOddSequenceOfTypeAndDay(moduloByTwo: Int = 1): Stream<Arguments> =
            Arrays.stream(DayOfWeek.values())
                .filter { it.ordinal < 5 }
                .map {
                    Arguments.of(
                        it,
                        if (it.ordinal % 2 == moduloByTwo) CertificateType.NDFL else CertificateType.LABOUR_BOOK
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
    fun `given correct CertificateType for dayOfWeek when processNextRequest then no Exceptions thrown`(
        dayOfWeek: DayOfWeek, certificateType: CertificateType
    ) {
        val incomeBox = LinkedList<CertificateRequest>()
        incomeBox.push(certificateRequest)
        injectValueToHrDepartment("incomeBox", incomeBox)

        val outcomeBox = mockk<LinkedList<Certificate>>(relaxed = true)
        injectValueToHrDepartment("outcomeBox", outcomeBox)

        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns certificateType

        assertDoesNotThrow { HrDepartment.processNextRequest(hrNumber) }
        verify { certificateRequest.process(hrNumber) }
        verify { outcomeBox.push(ofType(Certificate::class)) }
    }

    private fun injectValueToHrDepartment(fieldNameForInjection: String, injectedValue: Any) {
        val field = HrDepartment::class.java.getDeclaredField(fieldNameForInjection)
        field.isAccessible = true

        val modifiers = Field::class.java.getDeclaredField("modifiers")
        modifiers.isAccessible = true
        modifiers.setInt(field, field.modifiers and Modifier.FINAL.inv())

        field.set(HrDepartment, injectedValue)
    }

    @Test
    fun `given request not received when processNextRequest then NullPointerException thrown`() {
        assertThrows<NullPointerException> { HrDepartment.processNextRequest(1L) }
    }
}
