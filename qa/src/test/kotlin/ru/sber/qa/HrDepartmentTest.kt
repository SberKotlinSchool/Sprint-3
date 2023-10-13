package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.random.Random

class HrDepartmentTest {

    private val employeeNumber: Long = 1L

    private val certificateRequestLabourBook = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
    private val certificateRequestNdfl = CertificateRequest(employeeNumber, CertificateType.NDFL)

    @Test
    fun testRecieveRequestLabourBookException() {
        setClock("09-10-2023") // monday
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        setClock("11-10-2023") // wednesday
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        setClock("13-10-2023") // friday
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        setClock("14-10-2023") // saturday
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        setClock("15-10-2023") // sunday
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }


    }

    @Test
    fun testRecieveRequestNdflSuccess() {
        setClock("09-10-2023") // monday
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestNdfl) }

        setClock("11-10-2023") // wednesday
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestNdfl) }

        setClock("13-10-2023") // friday
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestNdfl) }

    }

    @Test
    fun testRecieveRequestNdflException() {
        setClock("10-10-2023") // tuesday
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestNdfl) }

        setClock("12-10-2023") // thursday
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestNdfl) }

        setClock("07-10-2023") // saturday
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestNdfl) }

        setClock("08-10-2023") // sunday
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestNdfl) }
    }

    @Test
    fun testRecieveRequestLabourBookSuccess() {
        setClock("10-10-2023") // tuesday
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        setClock("12-10-2023") // thursday
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestLabourBook) }
    }

    @Test
    fun testProcessNextRequest() {
        val incomeBox: LinkedList<CertificateRequest> = getPrivateField(HrDepartment, "incomeBox")
        val outcomeOutcome: LinkedList<CertificateRequest> = getPrivateField(HrDepartment, "outcomeOutcome")

        incomeBox.clear()
        outcomeOutcome.clear()

        val certificateRequest = mockk<CertificateRequest>()

        val certificateNDFL = Certificate(certificateRequestNdfl, Random.nextLong(), ByteArray(Random.nextInt(100)))

        every { certificateRequest.process(any()) } returns certificateNDFL

        incomeBox.push(certificateRequest)

        HrDepartment.processNextRequest(employeeNumber)

        assertEquals(0, incomeBox.size)
        assertEquals(1, outcomeOutcome.size)
        assertEquals(certificateNDFL, outcomeOutcome.poll())
        verify(exactly = 1) { certificateRequest.process(any()) }
    }

    private fun setClock(dateStr: String) {
        val localDate = LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        HrDepartment.clock =
            Clock.fixed(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getPrivateField(obj: Any, fieldName: String): T {
        val field = obj.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(obj) as T
    }

}