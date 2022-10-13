package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    @AfterEach
    fun eraseBoxes() {
        HrDepartment.incomeBox.clear()
        HrDepartment.outcomeOutcome.clear()
    }

    @Test
    fun receiveRequestAtWeekend(){
        val clockSunday = Clock.fixed(Instant.parse("2022-10-09T19:00:00.00Z"),ZoneOffset.UTC)
        HrDepartment.clock = clockSunday
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertFailsWith<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }

    }

    @Test
    fun receiveRequestNDFLonMonday(){
        val clockMonday = Clock.fixed(Instant.parse("2022-10-03T19:00:00.00Z"),ZoneOffset.UTC)
        HrDepartment.clock = clockMonday
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestNDFLonTuesday(){
        val clockTuesday = Clock.fixed(Instant.parse("2022-10-04T19:00:00.00Z"),ZoneOffset.UTC)
        HrDepartment.clock = clockTuesday
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertFailsWith<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLABOUR_BOOKonTuesday(){
        val clockTuesday = Clock.fixed(Instant.parse("2022-10-04T19:00:00.00Z"),ZoneOffset.UTC)
        HrDepartment.clock = clockTuesday
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestLABOUR_BOOKonMonday(){
        val clockMonday = Clock.fixed(Instant.parse("2022-10-03T19:00:00.00Z"),ZoneOffset.UTC)
        HrDepartment.clock = clockMonday
        val certificateRequest = mockk<CertificateRequest>()
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertFailsWith<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequestTest() {
        val hrEmployeeNumber = 5L
        val clockFriday = Clock.fixed(Instant.parse("2022-10-07T19:00:00.00Z"),ZoneOffset.UTC)
        HrDepartment.clock = clockFriday
        val certificateRequest = mockk<CertificateRequest>()
        val certificate = Certificate(certificateRequest, hrEmployeeNumber, Random.nextBytes(100))

        every {certificateRequest.certificateType} returns CertificateType.NDFL

        every {certificateRequest.process(hrEmployeeNumber)} returns certificate

        HrDepartment.receiveRequest(certificateRequest)
        HrDepartment.processNextRequest(hrEmployeeNumber)

        assert(HrDepartment.incomeBox.isEmpty())
        assertEquals(1, HrDepartment.outcomeOutcome.size)
        assertEquals(certificate, HrDepartment.outcomeOutcome[0])
    }
}