package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class CertificateRequestTest {

    private val certificateType = mockk<CertificateType>()

    @Test
    fun `process() test`() {
        // given
        mockkObject(Scanner)
        val employeeNumber = 1L
        val hrEmployeeNumber = 1L
        val scanData = ByteArray(1)

        // when
        every { Scanner.getScanData() } returns scanData
        val certificateRequest = CertificateRequest(employeeNumber, certificateType)
        val certificate = certificateRequest.process(hrEmployeeNumber)

        // then
        Assertions.assertEquals(certificate.certificateRequest, certificateRequest)
        Assertions.assertEquals(certificate.processedBy, hrEmployeeNumber)
        Assertions.assertEquals(certificate.data, scanData)
        unmockkObject(Scanner)
    }
}