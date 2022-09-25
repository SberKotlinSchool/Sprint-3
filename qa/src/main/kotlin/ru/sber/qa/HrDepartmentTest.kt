package src.main.kotlin.ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.sber.qa.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

internal class HrDepartmentTest {
    val mockedLocalDateTime = mockkStatic(LocalDateTime::class)
    private val mockedCertificateRequest = mockk<CertificateRequest>()
//    private val spy = spyk<HrDepartment>(recordPrivateCalls = true)
    val mockCertificate = mockk<Certificate>()
    private val hrDepartment = HrDepartment

    @ParameterizedTest()
    @ValueSource(ints = [6,7]) // Sunday - 6, Saturday - 7
    fun recieveRequestThrowWeekendDayException(day: Int) {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.of(day)

        assertThrows(WeekendDayException::class.java) {
            hrDepartment.receiveRequest(mockedCertificateRequest)
        }
    }

    @ParameterizedTest()
    @ValueSource(ints = [1,3,5]) // Monday - 1, WEDNESDAY - 3, Friday - 5
    fun recieveRequestForNDFL(day: Int) {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.of(day)

        every { mockedCertificateRequest.certificateType } returns CertificateType.NDFL

        //перерыв кучу информации, я не понял, как мокировать private поля, поэтому добавил геттер в HrDepartment
//        every { spy.getProperty("incomeBox") } returns LinkedList<CertificateRequest>()

        hrDepartment.receiveRequest(mockedCertificateRequest)

        assertTrue(hrDepartment.getIncomeBox().isNotEmpty())

    }

    @ParameterizedTest()
    @ValueSource(ints = [2,4]) // TUESDAY - 2, THURSDAY - 4
    fun recieveRequestForLabourBook(day: Int) {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.of(day)

        every { mockedCertificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        hrDepartment.receiveRequest(mockedCertificateRequest)

        assertTrue(hrDepartment.getIncomeBox().isNotEmpty())
    }

    @Test
    fun testThrowNotAllowReceiveRequestExceptionForLabourBook() {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY

        every { mockedCertificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(mockedCertificateRequest)
        }
    }

    @Test
    fun testThrowNotAllowReceiveRequestExceptionForNDFL() {
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY

        every { mockedCertificateRequest.certificateType } returns CertificateType.NDFL

        assertThrows(NotAllowReceiveRequestException::class.java) {
            hrDepartment.receiveRequest(mockedCertificateRequest)
        }
    }

    //с моками получается как то так, не понял, как очереди замокать полностью
    @Test
    fun processNextRequest() {
        val hrEmployee = 99999L

        every { mockedCertificateRequest.process(hrEmployee) } returns
                Certificate(mockedCertificateRequest,
                    hrEmployee,
                    byteArrayOf(100)
                )
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY

        every { mockedCertificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        hrDepartment.receiveRequest(mockedCertificateRequest)
        hrDepartment.processNextRequest(hrEmployee)

        verify(exactly = 1) { mockedCertificateRequest.process(hrEmployee) }

        assertTrue(hrDepartment.getIncomeBox().isEmpty())
        assertTrue(hrDepartment.getOutcomeOutcome().isNotEmpty())
    }
}