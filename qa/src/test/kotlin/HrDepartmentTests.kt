import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import ru.sber.qa.Certificate
import ru.sber.qa.CertificateRequest
import ru.sber.qa.CertificateType
import ru.sber.qa.HrDepartment
import ru.sber.qa.NotAllowReceiveRequestException
import ru.sber.qa.WeekendDayException
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.LinkedList
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val TEST_EMPLOYEE_NUMBER = 0L
private const val TEST_HR_EMPLOYEE_NUMBER = 666L

private const val TEST_BYTE_ARRAY_SIZE = 100
private val TEST_BYTES = ByteArray(TEST_BYTE_ARRAY_SIZE)

class HrDepartmentTests {

    @BeforeEach
    fun beforeTests() {
        mockkStatic(LocalDateTime::class)
    }

    @AfterEach
    fun afterTests() {
        HrDepartment.incomeBox.clear()
        HrDepartment.outcomeBox.clear()
        unmockkAll()
    }

    private fun createCertificateRequestMock(certificateTypeValue: CertificateType): CertificateRequest =
        mockk {
            every { employeeNumber } returns TEST_EMPLOYEE_NUMBER
            every { certificateType } returns certificateTypeValue
            every { process(TEST_HR_EMPLOYEE_NUMBER) } returns Certificate(this, TEST_HR_EMPLOYEE_NUMBER, TEST_BYTES)
        }

    @Test
    fun `test successful processing`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY
        val mockRequest = createCertificateRequestMock(CertificateType.NDFL)
        // push request to income queue
        HrDepartment.incomeBox.push(mockRequest)

        assertDoesNotThrow { HrDepartment.processNextRequest(TEST_HR_EMPLOYEE_NUMBER) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
        assertNotNull(HrDepartment.outcomeBox.singleOrNull())
        verify(exactly = 1) { mockRequest.process(TEST_HR_EMPLOYEE_NUMBER) }
    }

    @Test
    fun `test processNextRequest with empty queue`() {
        assertThrows<NullPointerException> { HrDepartment.processNextRequest(TEST_HR_EMPLOYEE_NUMBER) }
        assertNull(HrDepartment.outcomeBox.singleOrNull())
    }

    @Test
    fun `test NDFL processing on MONDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY
        val mockRequest = createCertificateRequestMock(CertificateType.NDFL)

        assertDoesNotThrow { HrDepartment.receiveRequest(mockRequest) }
        assertNotNull(HrDepartment.incomeBox.singleOrNull())
        verify(exactly = 1) { LocalDateTime.now(any<Clock>()).dayOfWeek }
    }

    @Test
    fun `test NDFL processing on WEDNESDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.WEDNESDAY
        val mockRequest = createCertificateRequestMock(CertificateType.NDFL)

        assertDoesNotThrow { HrDepartment.receiveRequest(mockRequest) }
        assertNotNull(HrDepartment.incomeBox.singleOrNull())
        verify(exactly = 1) { LocalDateTime.now(any<Clock>()).dayOfWeek }
    }

    @Test
    fun `test NDFL processing on FRIDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.FRIDAY
        val mockRequest = createCertificateRequestMock(CertificateType.NDFL)

        assertDoesNotThrow { HrDepartment.receiveRequest(mockRequest) }
        assertNotNull(HrDepartment.incomeBox.singleOrNull())
        verify(exactly = 1) { LocalDateTime.now(any<Clock>()).dayOfWeek }
    }

    @Test
    fun `test LABOUR_BOOK processing on TUESDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        val mockRequest = createCertificateRequestMock(CertificateType.LABOUR_BOOK)

        assertDoesNotThrow { HrDepartment.receiveRequest(mockRequest) }
        assertNotNull(HrDepartment.incomeBox.singleOrNull())
        verify(exactly = 1) { LocalDateTime.now(any<Clock>()).dayOfWeek }
    }

    @Test
    fun `test LABOUR_BOOK processing on THURSDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.THURSDAY
        val mockRequest = createCertificateRequestMock(CertificateType.LABOUR_BOOK)

        assertDoesNotThrow { HrDepartment.receiveRequest(mockRequest) }
        assertNotNull(HrDepartment.incomeBox.singleOrNull())
        verify(exactly = 1) { LocalDateTime.now(any<Clock>()).dayOfWeek }
    }

    @Test
    fun `test NDFL for NotAllowReceiveRequestException on TUESDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.TUESDAY
        val mockRequest = createCertificateRequestMock(CertificateType.NDFL)

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(mockRequest) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
    }

    @Test
    fun `test NDFL for NotAllowReceiveRequestException on THURSDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.THURSDAY
        val mockRequest = createCertificateRequestMock(CertificateType.NDFL)

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(mockRequest) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
    }

    @Test
    fun `test LABOUR_BOOK for NotAllowReceiveRequestException on MONDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.MONDAY
        val mockRequest = createCertificateRequestMock(CertificateType.LABOUR_BOOK)

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(mockRequest) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
    }

    @Test
    fun `test LABOUR_BOOK for NotAllowReceiveRequestException on WEDNESDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.WEDNESDAY
        val mockRequest = createCertificateRequestMock(CertificateType.LABOUR_BOOK)

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(mockRequest) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
    }

    @Test
    fun `test LABOUR_BOOK for NotAllowReceiveRequestException on FRIDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.FRIDAY
        val mockRequest = createCertificateRequestMock(CertificateType.LABOUR_BOOK)

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(mockRequest) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
    }

    @Test
    fun `test receiveRequest for WeekendDayException on SATURDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.SATURDAY
        val mockRequest: CertificateRequest = mockk(relaxed = true)

        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(mockRequest) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
    }

    @Test
    fun `test receiveRequest for WeekendDayException on SUNDAY`() {
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns DayOfWeek.SUNDAY
        val mockRequest: CertificateRequest = mockk(relaxed = true)

        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(mockRequest) }
        assertNull(HrDepartment.incomeBox.singleOrNull())
    }
}

@Suppress("UNCHECKED_CAST")
private val HrDepartment.incomeBox: LinkedList<CertificateRequest>
    get() = this::class.java.getDeclaredField("incomeBox")
        .apply { isAccessible = true }
        .get(this) as LinkedList<CertificateRequest>

@Suppress("UNCHECKED_CAST")
private val HrDepartment.outcomeBox: LinkedList<Certificate>
    get() = this::class.java.getDeclaredField("outcomeOutcome")
        .apply { isAccessible = true }
        .get(this) as LinkedList<Certificate>
