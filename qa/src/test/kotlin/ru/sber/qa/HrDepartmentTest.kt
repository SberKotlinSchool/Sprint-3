package ru.sber.qa

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream

class HrDepartmentTest {

    @MockK(relaxed = true)
    lateinit var certificateRequest: CertificateRequest

    private val hrNum = 1L

    companion object {
        @JvmStatic
        fun negativeSequence() = get()
        @JvmStatic
        fun positiveSequence() = get(0)

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
    private fun pasteField(name: String, value: Any) {
        val field = HrDepartment::class.java.getDeclaredField(name)
        field.isAccessible = true
        val modifiers = Field::class.java.getDeclaredField("modifiers")

        modifiers.isAccessible = true
        modifiers.setInt(field, field.modifiers and Modifier.FINAL.inv())
        field.set(HrDepartment, value)
    }

    @ParameterizedTest
    @MethodSource("positiveSequence")
    fun `AllowReceiveRequestException check`(
        dayOfWeek: DayOfWeek, certificateType: CertificateType
    ) {
        val incomeBox = LinkedList<CertificateRequest>()
        incomeBox.push(certificateRequest)
        pasteField("incomeBox", incomeBox)

        val outcomeBox = mockk<LinkedList<Certificate>>(relaxed = true)
        pasteField("outcomeOutcome", outcomeBox)

        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
        every { certificateRequest.certificateType } returns certificateType

        assertDoesNotThrow { HrDepartment.processNextRequest(hrNum) }
        verify { certificateRequest.process(hrNum) }
        verify { outcomeBox.push(ofType(Certificate::class)) }
    }
}