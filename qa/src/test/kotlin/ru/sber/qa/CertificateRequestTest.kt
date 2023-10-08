package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CertificateRequestTest {

    private val certificateType = mockk<CertificateType>()
    private val certificateRequest = CertificateRequest(employeeNumber = 1, certificateType = certificateType)
    @Test
    fun process() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns byteArrayOf(1)
        val certificate = certificateRequest.process(hrEmployeeNumber = 1)
        assertTrue(certificate == Certificate(certificateRequest, 1, byteArrayOf(1)))
    }

    @Test
    fun getEmployeeNumber() {
        assertEquals(1, certificateRequest.employeeNumber)
    }

    @Test
    fun getCertificateType() {
        assertNotNull(certificateType)
    }
}