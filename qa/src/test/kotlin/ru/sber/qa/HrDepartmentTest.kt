package ru.sber.qa

import io.mockk.every
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.LinkedList
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class HrDepartmentTest {

    @SpyK
    var certificateRequest = CertificateRequest(11L, CertificateType.NDFL)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestSuccessNDFL() {
        val clockMon = Clock.fixed(Instant.parse("2022-11-14T21:17:00.00Z"), ZoneOffset.UTC)
        HrDepartment.clock = clockMon
        HrDepartment.receiveRequest(certificateRequest)
        val fieldBox = HrDepartment.javaClass.getDeclaredField("incomeBox")
        fieldBox.trySetAccessible()
        val incomeBox = fieldBox.get(HrDepartment) as LinkedList<*>

        assertEquals(certificateRequest, incomeBox.poll())
    }

    @Test
    fun receiveRequestSuccessLabourBook() {
        val clockTue = Clock.fixed(Instant.parse("2022-11-15T21:17:00.00Z"), ZoneOffset.UTC)
        HrDepartment.clock = clockTue
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        HrDepartment.receiveRequest(certificateRequest)
        val fieldBox = HrDepartment.javaClass.getDeclaredField("incomeBox")
        fieldBox.trySetAccessible()
        val incomeBox = fieldBox.get(HrDepartment) as LinkedList<*>

        assertEquals(certificateRequest, incomeBox.poll())
    }

    @Test
    fun receiveRequestWeekendDayException() {
        var clockFix = Clock.fixed(Instant.parse("2022-11-12T21:17:00.00Z"), ZoneOffset.UTC)
        HrDepartment.clock = clockFix
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }

        clockFix = Clock.fixed(Instant.parse("2022-11-13T21:17:00.00Z"), ZoneOffset.UTC)
        HrDepartment.clock = clockFix
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestNotAllowReceiveRequestException() {
        val clockMon = Clock.fixed(Instant.parse("2022-11-14T21:17:00.00Z"), ZoneOffset.UTC)
        HrDepartment.clock = clockMon
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequest() {
        val certificateMock = mockk<Certificate>()
        val certificateRequestMock = mockk<CertificateRequest>()

        every { certificateRequestMock.process(any()) } returns certificateMock
        val fieldBox = HrDepartment.javaClass.getDeclaredField("incomeBox")
        fieldBox.trySetAccessible()
        val incomeBox = fieldBox.get(HrDepartment) as LinkedList<CertificateRequest>
        incomeBox.push(certificateRequestMock)

        HrDepartment.processNextRequest(11L)
        val fieldOutcome = HrDepartment.javaClass.getDeclaredField("outcomeOutcome")
        fieldOutcome.trySetAccessible()
        val outcomeList = fieldOutcome.get(HrDepartment) as LinkedList<*>

        assertEquals(certificateMock, outcomeList.poll())
    }
}