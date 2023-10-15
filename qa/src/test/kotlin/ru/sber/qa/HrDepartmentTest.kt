package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.*
import java.util.*
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    private var certificateRequest: CertificateRequest = mockk()

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SATURDAY", "SUNDAY"])
    fun `should receiveRequest throws WeekendDayException`(day: DayOfWeek) {
        //given
        HrDepartment.clock = getClock(day)

        //when
        //then
        assertFailsWith<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }


    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun `should receiveRequest for NDFL throws NotAllowReceiveRequestException `(day: DayOfWeek) {
        //given
        HrDepartment.clock = getClock(day)
        every { certificateRequest.certificateType }.returns(CertificateType.NDFL)

        //when
        //then
        assertFailsWith<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun `should receiveRequest for LABOUR_BOOK throws NotAllowReceiveRequestException `(day: DayOfWeek) {
        //given
        HrDepartment.clock = getClock(day)
        every { certificateRequest.certificateType }.returns(CertificateType.LABOUR_BOOK)

        //when
        //then
        assertFailsWith<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun `should receiveRequest for NDFL add incomeBox`(day: DayOfWeek) {
        //given
        HrDepartment.clock = getClock(day)
        every { certificateRequest.certificateType }.returns(CertificateType.NDFL)

        //when
        HrDepartment.receiveRequest(certificateRequest)
        //then
        assertEquals(certificateRequest, getValue("incomeBox").first)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun `should receiveRequest for LABOUR_BOOK add incomeBox`(day: DayOfWeek) {
        //given
        HrDepartment.clock = getClock(day)
        every { certificateRequest.certificateType }.returns(CertificateType.LABOUR_BOOK)

        //when
        HrDepartment.receiveRequest(certificateRequest)

        //then
        assertEquals(certificateRequest, getValue("incomeBox").first)
    }


    @Suppress("UNCHECKED_CAST")
    @Test
    fun `should processNextRequest add outcomeOutcome`() {
        //given
        val list: LinkedList<CertificateRequest> = getValue("incomeBox") as LinkedList<CertificateRequest>
        list.clear()
        list.add(certificateRequest)

        val certificate = mockk<Certificate>()
        every { certificateRequest.process(any()) } returns certificate

        //when
        HrDepartment.processNextRequest(1L)

        //then
        assertEquals(certificate, getValue("outcomeOutcome").first)
    }


    private fun getValue(name: String): LinkedList<*> {
        HrDepartment.javaClass.getDeclaredField(name).let { field ->
            field.isAccessible = true
            return field.get(name) as LinkedList<*>
        }
    }

    private fun getClock(day: DayOfWeek): Clock {
        val zone = ZoneId.of("Europe/Moscow")

        return when (day) {
            DayOfWeek.MONDAY -> Clock.fixed(Instant.parse("2023-10-16T12:00:00.00Z"), zone)
            DayOfWeek.TUESDAY -> Clock.fixed(Instant.parse("2023-10-17T12:00:00.00Z"), zone)
            DayOfWeek.WEDNESDAY -> Clock.fixed(Instant.parse("2023-10-18T12:00:00.00Z"), zone)
            DayOfWeek.THURSDAY -> Clock.fixed(Instant.parse("2023-10-19T12:00:00.00Z"), zone)
            DayOfWeek.FRIDAY -> Clock.fixed(Instant.parse("2023-10-20T12:00:00.00Z"), zone)
            DayOfWeek.SATURDAY -> Clock.fixed(Instant.parse("2023-10-21T12:00:00.00Z"), zone)
            DayOfWeek.SUNDAY -> Clock.fixed(Instant.parse("2023-10-22T12:00:00.00Z"), zone)
        }
    }

}