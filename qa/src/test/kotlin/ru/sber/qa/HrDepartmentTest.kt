package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

internal class HrDepartmentTest {
    private val hrEmployeeNumber: Long = 1L

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestNdflOnWeekEnd() {
        mockkStatic(LocalDateTime::class)
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SUNDAY
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SATURDAY
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLabourBookOnWeekEnd() {
        mockkStatic(LocalDateTime::class)
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.LABOUR_BOOK)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SUNDAY
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SATURDAY
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestNdflOnWorkDay() {
        mockkStatic(LocalDateTime::class)
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.WEDNESDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.FRIDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLabourBookOnWorkDay() {
        mockkStatic(LocalDateTime::class)
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.LABOUR_BOOK)

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.TUESDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.WEDNESDAY
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }

        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.FRIDAY
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequest() {
        var incomeBox: LinkedList<CertificateRequest> = getPrivateFiled(HrDepartment, "incomeBox")
        var outcomeOutcome: LinkedList<CertificateRequest> = getPrivateFiled(HrDepartment, "outcomeOutcome")

        incomeBox.clear()
        outcomeOutcome.clear()

        val certificateRequest = mockk<CertificateRequest>()
        val certificateNDFL = Certificate(CertificateRequest(hrEmployeeNumber, CertificateType.NDFL), Random.nextLong(), ByteArray(100))
        every { certificateRequest.process(any()) } returns certificateNDFL

        incomeBox.push(certificateRequest)

        HrDepartment.processNextRequest(hrEmployeeNumber)

        assertEquals(0, incomeBox.size)
        assertEquals(1, outcomeOutcome.size)
        assertEquals(certificateNDFL, outcomeOutcome.poll())
    }

    // чит, конечно, но к private полям достучаться надо
    private fun <T> getPrivateFiled(obj: Any, fieldName: String): T {
        return obj.javaClass.getDeclaredField(fieldName).let {
            it.isAccessible = true
            return@let it.get(this)
        } as T
    }
}