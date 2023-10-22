package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.test.assertEquals

class HrDepartmentTest {
    private lateinit var certificateRequest: CertificateRequest
    private var hrDepartment = HrDepartment

    @BeforeEach
    fun setUp() {
        certificateRequest = mockk()

        hrDepartment.clock = null
        getIncomeBox().clear()
        getOutcomeOutcome().clear()
    }

    @Test
    fun receiveRequest() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        hrDepartment.clock =
            Clock.fixed(Instant.parse("2023-10-06T20:00:00.00Z"), ZoneId.of("Europe/Moscow")) // пятница

        hrDepartment.receiveRequest(certificateRequest)

        val incomeBox = getIncomeBox()

        assertEquals(1, incomeBox.size)
    }

    @Test
    fun processNextRequest() {
        val hrEmployeeNumber = 7L
        val certificate = Certificate(certificateRequest, hrEmployeeNumber, byteArrayOf(8))
        every { certificateRequest.process(any()) } returns certificate
        hrDepartment.clock =
            Clock.fixed(Instant.parse("2023-10-02T20:00:00.00Z"), ZoneId.of("Europe/Moscow")) // понедельник
        val incomeBox = getIncomeBox()
        incomeBox.add(certificateRequest)
        val outcomeOutcome = getOutcomeOutcome()

        hrDepartment.processNextRequest(hrEmployeeNumber)

        assertEquals(0, incomeBox.size)
        assertEquals(1, outcomeOutcome.size)
    }

    @ParameterizedTest
    @ValueSource(strings = ["2023-10-07T20:00:00.00Z", "2023-10-08T20:00:00.00Z"]) // суббота и воскресенье
    fun receiveRequestThrowsWeekendDayException(date: String) {
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        hrDepartment.clock =
            Clock.fixed(Instant.parse(date), ZoneId.of("Europe/Moscow"))

        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["2023-10-03T20:00:00.00Z", "2023-10-05T20:00:00.00Z"]) // вторник и четверг
    fun receiveRequestThrowsNotAllowReceiveRequestExceptionForNdfl(date: String) {
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        hrDepartment.clock =
            Clock.fixed(Instant.parse(date), ZoneId.of("Europe/Moscow"))

        assertThrows<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["2023-10-02T20:00:00.00Z", "2023-10-04T20:00:00.00Z", "2023-10-06T20:00:00.00Z"]) // понедельник, среда и пятница
    fun receiveRequestThrowsNotAllowReceiveRequestExceptionForLabourBook(date: String) {
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        hrDepartment.clock =
            Clock.fixed(Instant.parse(date), ZoneId.of("Europe/Moscow")) // понедельник

        assertThrows<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequest) }
    }

    private fun getIncomeBox(): LinkedList<CertificateRequest> {
        return HrDepartment.getPrivateField("incomeBox")
    }

    private fun getOutcomeOutcome(): LinkedList<Certificate> {
        return HrDepartment.getPrivateField("outcomeOutcome")
    }

    private fun <T> Any.getPrivateField(fieldName: String): T {
        val field = this.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(this) as T
    }
}
