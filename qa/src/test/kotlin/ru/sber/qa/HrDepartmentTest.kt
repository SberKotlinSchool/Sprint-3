package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.time.*
import java.util.*

internal class HrDepartmentTest {
    val ZONE = ZoneId.systemDefault()
    val EMPLOYEE = 123L
    val NDFL_REQ = CertificateRequest(EMPLOYEE, CertificateType.NDFL)
    val LABOUR_BOOK_REQ = CertificateRequest(EMPLOYEE, CertificateType.LABOUR_BOOK)

    @BeforeEach
    fun prepare() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns "some data".toByteArray()
    }

    @AfterEach
    fun release() {
        unmockkAll()
    }

    @Test
    fun `НДФЛ в понедельник`() {
        HrDepartment.clock = mockClock("2023-10-09T00:00:00") // понедельник
        assertDoesNotThrow { HrDepartment.receiveRequest(NDFL_REQ) }
    }

    @Test
    fun `трудовая в четверг`() {
        HrDepartment.clock = mockClock("2023-10-12T00:00:00") // четверг
        assertDoesNotThrow { HrDepartment.receiveRequest(LABOUR_BOOK_REQ) }
    }

    @Test
    fun `выходной`() {
        HrDepartment.clock = mockClock("2023-10-14T00:00:00") // суббота
        assertThrows(WeekendDayException::class.java, { HrDepartment.receiveRequest(LABOUR_BOOK_REQ) })
    }

    @Test
    fun `НДФЛ в четверг`() {
        HrDepartment.clock = mockClock("2023-10-12T00:00:00") // четверг
        assertThrows(NotAllowReceiveRequestException::class.java, { HrDepartment.receiveRequest(NDFL_REQ) })
    }

    @Test
    fun `трудовая в понедельник`() {
        HrDepartment.clock = mockClock("2023-10-09T00:00:00") // понедельник
        assertThrows(NotAllowReceiveRequestException::class.java, { HrDepartment.receiveRequest(LABOUR_BOOK_REQ) })
    }

    @Test
    fun `запрос добавился в очередь`() {
        val incomeBoxMock = LinkedList<CertificateRequest>()

        mockPrivateStaticFinalField(HrDepartment::class.java.getDeclaredField("incomeBox"), incomeBoxMock)

        HrDepartment.clock = mockClock("2023-10-09T00:00:00") // понедельник
        assertEquals(0, incomeBoxMock.size)

        HrDepartment.receiveRequest(NDFL_REQ)

        assertEquals(1, incomeBoxMock.size)
        assertEquals(NDFL_REQ, incomeBoxMock[0])
    }

    @Test
    fun `обрабатываем по 1 запросу`() {
        val incomeBoxMock = LinkedList<CertificateRequest>()
        incomeBoxMock.add(NDFL_REQ)
        val outcomeOutcomeMock = LinkedList<Certificate>()

        mockPrivateStaticFinalField(HrDepartment::class.java.getDeclaredField("incomeBox"), incomeBoxMock)
        mockPrivateStaticFinalField(HrDepartment::class.java.getDeclaredField("outcomeOutcome"), outcomeOutcomeMock)

        HrDepartment.processNextRequest(EMPLOYEE)

        assertEquals(1, outcomeOutcomeMock.size)
        assertEquals(0, incomeBoxMock.size)
        val certificate = outcomeOutcomeMock[0]
        assertEquals(certificate.processedBy, EMPLOYEE)
    }

    private fun mockClock(pattern: String) : Clock {
        val dateStub = ZonedDateTime.of(LocalDateTime.parse(pattern), ZONE)
        return Clock.fixed(dateStub.toInstant(), ZONE)
    }

    private fun mockPrivateStaticFinalField(field: Field, valueToSet: Any) {
        field.setAccessible(true)
        val modifiersField = Field::class.java.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL.inv())
        field.set(null, valueToSet)
    }
}