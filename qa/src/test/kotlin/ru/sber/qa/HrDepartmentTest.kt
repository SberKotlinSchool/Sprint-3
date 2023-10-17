package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.util.*
import kotlin.test.assertEquals

class HrDepartmentTest {
    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    private fun init() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns "111".toByteArray()
        val tuesdayClock = Clock.fixed(Instant.parse("2023-10-10T18:00:00Z"), ZoneId.of("Europe/Moscow"))
        HrDepartment.clock = tuesdayClock
        certificateRequest = CertificateRequest(123, CertificateType.LABOUR_BOOK)
    }

    @Test
    fun receiveRequestTest() {
        HrDepartment.receiveRequest(certificateRequest)
        assertEquals(1, getFieldValue("incomeBox").size)
    }

    @Test
    fun receiveRequestThrowsExceptionIfWeekend() {
        // воскресенье
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-08T18:00:00Z"), ZoneId.of("Europe/Moscow"))
        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestThrowsExceptionIfNotAllowed() {
        // понедельник недопустим для принятия справки по трудовой
        HrDepartment.clock = Clock.fixed(Instant.parse("2023-10-09T18:00:00Z"), ZoneId.of("Europe/Moscow"))
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun processNextRequestTest() {
        val list: LinkedList<CertificateRequest> = getFieldValue("incomeBox") as LinkedList<CertificateRequest>
        list.clear()
        list.add(certificateRequest)
        HrDepartment.processNextRequest(1)
        assertEquals(1, getFieldValue("outcomeOutcome").size)
    }

    private fun getFieldValue(fieldName: String): LinkedList<*> {
        val field = HrDepartment.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(fieldName) as LinkedList<*>
    }
}