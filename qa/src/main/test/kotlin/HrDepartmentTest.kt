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
    fun receiveRequestNdfl() {
        // monday
        setClock("09-10-2023")
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestNdfl) }

        // tuesday
        setClock("10-10-2023")
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestNdfl) }

        // wednesday
        setClock("11-10-2023")
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestNdfl) }

        // thursday
        setClock("12-10-2023")
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestNdfl) }

        // friday
        setClock("13-10-2023")
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestNdfl) }

        // saturday
        setClock("14-10-2023")
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestNdfl) }

        // sunday
        setClock("15-10-2023")
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestNdfl) }
    }

    @Test
    fun receiveRequestLabourBook() {
        // monday
        setClock("09-10-2023")
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        // tuesday
        setClock("10-10-2023")
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        // wednesday
        setClock("11-10-2023")
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        // thursday
        setClock("12-10-2023")
        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        // friday
        setClock("13-10-2023")
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        // saturday
        setClock("14-10-2023")
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }

        // sunday
        setClock("15-10-2023")
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequestLabourBook) }
    }

    @Test
    fun processNextRequest() {
        // given
        val certificateNDFL = Certificate(certificateRequestNdfl, Random.nextLong(), ByteArray(Random.nextInt(100)))
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.process(any()) } returns certificateNDFL

        val incomeBox: LinkedList<CertificateRequest> = getPrivateField(HrDepartment, "incomeBox")
        val outcomeOutcome: LinkedList<CertificateRequest> = getPrivateField(HrDepartment, "outcomeOutcome")
        incomeBox.clear()
        outcomeOutcome.clear()

        incomeBox.push(certificateRequest)

        // when
        HrDepartment.processNextRequest(employeeNumber)

        // then
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