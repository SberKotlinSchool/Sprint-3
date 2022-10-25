package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class HrDepartmentTest {
    private val certificateRequest = mockk<CertificateRequest>()
    private val certificate = mockk<Certificate>()

    @BeforeEach
    fun setUp() {
        mockkObject(HrDepartment)
    }

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
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        val hrEmployeeNumber = 1L
        every { certificateRequest.process(hrEmployeeNumber) } returns certificate
        HrDepartment.receiveRequest(certificateRequest)
        HrDepartment.processNextRequest(hrEmployeeNumber)
        verify { certificateRequest.process(hrEmployeeNumber) }
    }
}