package ru.sber.qa

import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.NullPointerException
import java.time.*

@ExtendWith(MockKExtension::class)
internal class HrDepartmentTest {
    lateinit var hrDepartment: HrDepartment

    @BeforeEach
    fun setUp() {
        hrDepartment = spyk(HrDepartment, recordPrivateCalls = true)
    }

    @AfterEach
    fun tearDown() {
        hrDepartment.incomeBox.clear()
        hrDepartment.outcomeOutcome.clear()
        unmockkAll()
    }

    private fun receiveRequestTemplate(
        dayOfWeek: DayOfWeek,
        certificateType: CertificateType,
        employeeNumber: Long
    ) {
        hrDepartment.clock = Clock.fixed(
            LocalDate.parse("2022-10-" + String.format("%02d", dayOfWeek.getValue() + 2))
                .atStartOfDay(ZoneId.of("Europe/Paris"))
                .toInstant(),
            ZoneId.of("Europe/Paris")
        )
        hrDepartment.receiveRequest(CertificateRequest(employeeNumber, certificateType))
    }


    @Test
    fun `receiveRequest Labour on MONDAY must fail`() {
        assertThrows<NotAllowReceiveRequestException> {
            receiveRequestTemplate(DayOfWeek.MONDAY, CertificateType.LABOUR_BOOK, 1L)
        }
    }

    @Test
    fun `receiveRequest Labour on TUESDAY success`() {
        receiveRequestTemplate(DayOfWeek.TUESDAY, CertificateType.LABOUR_BOOK, 1L)
        assertEquals(hrDepartment.incomeBox.size, 1)

    }

    @Test
    fun `receiveRequest Labour on WEDNESDAY must fail`() {
        assertThrows<NotAllowReceiveRequestException> {
            receiveRequestTemplate(DayOfWeek.WEDNESDAY, CertificateType.LABOUR_BOOK, 1L)
        }
    }

    @Test
    fun `receiveRequest Labour on THURSDAY success`() {
        receiveRequestTemplate(DayOfWeek.THURSDAY, CertificateType.LABOUR_BOOK, 1L)
        assertEquals(hrDepartment.incomeBox.size, 1)
    }

    @Test
    fun `receiveRequest Labour on FRIDAY must fail`() {
        assertThrows<NotAllowReceiveRequestException> {
            receiveRequestTemplate(DayOfWeek.FRIDAY, CertificateType.LABOUR_BOOK, 1L)
        }
    }

    @Test
    fun `receiveRequest Labour on SUNDAY must fail`() {
        assertThrows<WeekendDayException> {
            receiveRequestTemplate(DayOfWeek.SUNDAY, CertificateType.LABOUR_BOOK, 1L)
        }
    }

    @Test
    fun `receiveRequest Labour on SATURDAY must fail`() {
        assertThrows<WeekendDayException> {
            receiveRequestTemplate(DayOfWeek.SATURDAY, CertificateType.LABOUR_BOOK, 1L)
        }
    }

    @Test
    fun `receiveRequest NDFL on MONDAY success`() {
        receiveRequestTemplate(DayOfWeek.MONDAY, CertificateType.NDFL, 1L)
        assertEquals(hrDepartment.incomeBox.size, 1)
    }

    @Test
    fun `receiveRequest NDFL on TUESDAY must fail`() {
        assertThrows<NotAllowReceiveRequestException> {
            receiveRequestTemplate(DayOfWeek.TUESDAY, CertificateType.NDFL, 1L)
        }

    }

    @Test
    fun `receiveRequest NDFL on WEDNESDAY success`() {
        receiveRequestTemplate(DayOfWeek.WEDNESDAY, CertificateType.NDFL, 1L)
        assertEquals(hrDepartment.incomeBox.size, 1)
    }

    @Test
    fun `receiveRequest NDFL on THURSDAY must fail`() {
        assertThrows<NotAllowReceiveRequestException> {
            receiveRequestTemplate(DayOfWeek.THURSDAY, CertificateType.NDFL, 1L)
        }
    }

    @Test
    fun `receiveRequest NDFL on FRIDAY success`() {
        receiveRequestTemplate(DayOfWeek.FRIDAY, CertificateType.NDFL, 1L)
        assertEquals(hrDepartment.incomeBox.size, 1)
    }

    @Test
    fun `receiveRequest NDFL on SUNDAY must fail`() {
        assertThrows<WeekendDayException> {
            receiveRequestTemplate(DayOfWeek.SUNDAY, CertificateType.NDFL, 1L)
        }
    }

    @Test
    fun `receiveRequest NDFL on SATURDAY must fail`() {
        assertThrows<WeekendDayException> {
            receiveRequestTemplate(DayOfWeek.SATURDAY, CertificateType.NDFL, 1L)
        }
    }


    @Test
    fun `processNextRequest nullpointer if incomeBox is empty`() {
        assertThrows<NullPointerException> { hrDepartment.processNextRequest(2L) }
    }

    @Test
    fun `processNextRequest success`() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns Random.nextBytes(100)

        receiveRequestTemplate(DayOfWeek.FRIDAY, CertificateType.NDFL, 1L)

        hrDepartment.processNextRequest(2L)

        assertTrue(hrDepartment.incomeBox.isEmpty())
        assertEquals(hrDepartment.outcomeOutcome.size, 1)
    }

}