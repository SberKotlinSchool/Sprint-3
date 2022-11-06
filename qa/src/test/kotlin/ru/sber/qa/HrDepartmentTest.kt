package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

fun getClock(time: String): Clock =
    Clock.fixed(Instant.parse(time), ZoneId.of("UTC"))

internal class HrDepartmentTest {
    private val certificateRequest = mockk<CertificateRequest>()

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `receiveRequest Throws WeekendDayException`() {
        assertThrows<WeekendDayException> {
            HrDepartment.clock = getClock("2022-10-30T10:15:30.00Z")
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `first throw NotAllowReceiveRequestException`() {
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.clock = getClock("2022-10-25T10:15:30.00Z") //TUESDAY
            every { certificateRequest.certificateType } returns CertificateType.NDFL
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `second throw NotAllowReceiveRequestException`() {
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.clock = getClock("2022-10-24T10:15:30.00Z") //MONDAY
            every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `success push and poll`() {
        HrDepartment.clock = getClock("2022-10-24T10:15:30.00Z") //MONDAY
        val hrEmployeeNumber = 1L
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(1)
        val certificateR = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)
        HrDepartment.receiveRequest(certificateR)
        HrDepartment.processNextRequest(hrEmployeeNumber)
        verify { certificateR.process(hrEmployeeNumber) }
    }
}
