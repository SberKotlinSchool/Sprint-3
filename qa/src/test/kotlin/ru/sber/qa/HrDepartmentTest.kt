package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.util.*

internal class HrDepartmentTest {
    private lateinit var certificateRequest: CertificateRequest

    private lateinit var incomeBox: LinkedList<CertificateRequest>
    private lateinit var outcomeOutcome: LinkedList<Certificate>


    @BeforeEach
    fun beforeTests() {
        mockkStatic(Clock::class)
        mockkStatic(LocalDateTime::class)
        certificateRequest = mockk()

        incomeBox = HrDepartment.getPrivateFiled("incomeBox")
        outcomeOutcome = HrDepartment.getPrivateFiled("outcomeOutcome")
    }

    @Test
    fun `receiveRequest() should throw WeekendDayException`() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        listOf(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY).forEach { day ->
            HrDepartment.clock = getFixedClock(day)
            assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
        }
    }

    @Test
    fun `receiveRequest() should NotAllowReceiveRequestException`() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY).forEach { day ->
            HrDepartment.clock = getFixedClock(day)
            assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
        }

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY).forEach { day ->
            HrDepartment.clock = getFixedClock(day)
            assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
        }
    }

    @Test
    fun `receiveRequest() should not NotAllowReceiveRequestException`() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY).forEach { day ->
            HrDepartment.clock = getFixedClock(day)
            assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        }

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY).forEach { day ->
            HrDepartment.clock = getFixedClock(day)
            assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        }
    }

    @Test
    fun `receiveRequest() should add CertificateRequest to incomeBox`() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { certificateRequest.employeeNumber } returns 0L

        HrDepartment.receiveRequest(certificateRequest)
        assertTrue { incomeBox.size > 0 }
        assertTrue { incomeBox.contains(certificateRequest) }
    }

    @Test
    fun `processNextRequest() should add Certificate to outcomeOutcome from incomeBox`() {
        val certificateRequestMockk: CertificateRequest = mockk()
        val certificateMockk: Certificate = mockk()
        val hrEmployeeNumber = 0L

        every { certificateRequestMockk.process(hrEmployeeNumber) } returns certificateMockk

        incomeBox.push(certificateRequestMockk)

        HrDepartment.processNextRequest(hrEmployeeNumber)

        assertTrue { outcomeOutcome.size > 0 }
        assertTrue { outcomeOutcome.contains(certificateMockk) }
    }


    @AfterEach
    fun afterTests() {
        unmockkAll()
    }
}


private fun <T> Any.getPrivateFiled(fieldName: String): T {
    val field = this.javaClass.getDeclaredField(fieldName)
    field.isAccessible = true
    return field.get(this) as T
}


private fun getFixedClock(dayOfWeek: DayOfWeek) = Clock.fixed(
    Instant.parse(
        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> "2022-11-13T12:00:00.000Z"
            DayOfWeek.MONDAY -> "2022-11-14T12:00:00.000Z"
            DayOfWeek.TUESDAY -> "2022-11-15T12:00:00.000Z"
            DayOfWeek.WEDNESDAY -> "2022-11-16T12:00:00.000Z"
            DayOfWeek.THURSDAY -> "2022-11-17T12:00:00.000Z"
            DayOfWeek.FRIDAY -> "2022-11-18T12:00:00.000Z"
            DayOfWeek.SATURDAY -> "2022-11-19T12:00:00.000Z"
        }
    ), ZoneId.of("UTC")
)

