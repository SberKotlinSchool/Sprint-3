package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.time.*
import java.util.*

internal class HrDepartmentTest {

    lateinit var incomeBox: LinkedList<CertificateRequest>
    lateinit var outcomeOutcome: LinkedList<Certificate>
    lateinit var certificateRequestNDFL: CertificateRequest
    lateinit var certificateNDFL: Certificate
    lateinit var certificateRequestLABOUR: CertificateRequest
    lateinit var certificateLABOUR: Certificate
    lateinit var hrDepartment: HrDepartment

    val clockMonday = Clock.fixed(Instant.parse("2022-09-19T10:00:00.00Z"), ZoneId.of("Europe/Paris"))
    val clockTuesday = Clock.fixed(Instant.parse("2022-09-20T10:00:00.00Z"), ZoneId.of("Europe/Paris"))
    val clockThursday = Clock.fixed(Instant.parse("2022-09-21T10:00:00.00Z"), ZoneId.of("Europe/Paris"))
    val clockWednesday = Clock.fixed(Instant.parse("2022-09-22T10:00:00.00Z"), ZoneId.of("Europe/Paris"))
    val clockFriday = Clock.fixed(Instant.parse("2022-09-23T10:00:00.00Z"), ZoneId.of("Europe/Paris"))
    val clockSaturday = Clock.fixed(Instant.parse("2022-09-24T10:00:00.00Z"), ZoneId.of("Europe/Paris"))
    val clockSunday = Clock.fixed(Instant.parse("2022-09-25T10:00:00.00Z"), ZoneId.of("Europe/Paris"))


    @BeforeEach
    fun setUp() {
        hrDepartment = spyk()
        incomeBox = hrDepartment.incomeBox
        outcomeOutcome = hrDepartment.outcomeOutcome

        certificateNDFL = mockk()
        certificateRequestNDFL = mockk()
        every { certificateRequestNDFL.process(any()) } returns certificateNDFL
        every { certificateRequestNDFL.certificateType } returns CertificateType.NDFL
        every { certificateNDFL.certificateRequest.certificateType } returns CertificateType.NDFL

        certificateLABOUR = mockk()
        certificateRequestLABOUR = mockk()
        every { certificateRequestLABOUR.process(any()) } returns certificateLABOUR
        every { certificateRequestLABOUR.certificateType } returns CertificateType.LABOUR_BOOK
        every { certificateLABOUR.certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
    }

    @Test
    fun `receiveRequest() accept NDFL request on Monday`() {
        hrDepartment.clock = clockMonday
        hrDepartment.receiveRequest(certificateRequestNDFL)

        assertEquals(incomeBox.size, 1)
        assertEquals(incomeBox.poll().certificateType, CertificateType.NDFL)
    }

    @Test
    fun `receiveRequest() accept LABOUR_BOOK request on Tuesday`() {
        hrDepartment.clock = clockTuesday
        hrDepartment.receiveRequest(certificateRequestLABOUR)

        assertEquals(incomeBox.size, 1)
        assertEquals(incomeBox.poll().certificateType, CertificateType.LABOUR_BOOK)
    }

    @Test
    fun `receiveRequest() accept NDFL request on Thursday`() {
        hrDepartment.clock = clockThursday
        hrDepartment.receiveRequest(certificateRequestNDFL)

        assertEquals(incomeBox.size, 1)
        assertEquals(incomeBox.poll().certificateType, CertificateType.NDFL)
    }

    @Test
    fun `receiveRequest() accept LABOUR_BOOK request on Wednesday`() {
        hrDepartment.clock = clockWednesday
        hrDepartment.receiveRequest(certificateRequestLABOUR)

        assertEquals(incomeBox.size, 1)
        assertEquals(incomeBox.poll().certificateType, CertificateType.LABOUR_BOOK)
    }

    @Test
    fun `receiveRequest() accept NDFL request on Friday`() {
        hrDepartment.clock = clockFriday
        hrDepartment.receiveRequest(certificateRequestNDFL)

        assertEquals(incomeBox.size, 1)
        assertEquals(incomeBox.poll().certificateType, CertificateType.NDFL)
    }

    @Test
    fun `receiveRequest() reject LABOUR_BOOK rejects on Monday`() {
        hrDepartment.clock = clockMonday
        assertThrows<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequestLABOUR) }
    }

    @Test
    fun `receiveRequest() reject NDFL rejects on Tuesday`() {
        hrDepartment.clock = clockTuesday
        assertThrows<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequestNDFL) }
    }

    @Test
    fun `receiveRequest() reject LABOUR_BOOK rejects on Thursday`() {
        hrDepartment.clock = clockThursday
        assertThrows<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequestLABOUR) }
    }

    @Test
    fun `receiveRequest() reject NDFL rejects on Wednesday`() {
        hrDepartment.clock = clockWednesday
        assertThrows<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequestNDFL) }
    }

    @Test
    fun `receiveRequest() reject LABOUR_BOOK rejects on Saturday`() {
        hrDepartment.clock = clockSaturday
        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(certificateRequestLABOUR) }
    }

    @Test
    fun `receiveRequest() reject LABOUR_BOOK rejects on Sunday`() {
        hrDepartment.clock = clockSunday
        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(certificateRequestLABOUR) }
    }

    @Test
    fun `receiveRequest() reject NDFL rejects on Saturday`() {
        hrDepartment.clock = clockSaturday
        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(certificateRequestNDFL) }
    }

    @Test
    fun `receiveRequest() reject NDFL rejects on Sunday`() {
        hrDepartment.clock = clockSunday
        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(certificateRequestNDFL) }
    }

    @Test
    fun processNextRequestNDFL() {
        incomeBox.push(certificateRequestNDFL)
        hrDepartment.processNextRequest(1L)

        assertTrue(outcomeOutcome.size == 1)
        assertEquals(certificateNDFL, outcomeOutcome.poll())
    }

    @Test
    fun processNextRequestLABOUR() {
        incomeBox.push(certificateRequestLABOUR)
        hrDepartment.processNextRequest(1L)

        assertTrue(outcomeOutcome.size == 1)
        assertEquals(certificateLABOUR, outcomeOutcome.poll())
    }
}