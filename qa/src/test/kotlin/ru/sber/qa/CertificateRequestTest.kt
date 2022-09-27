package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

@MockeryOfTesting
internal class CertificateRequestTest {

    private val employeeNumber = 1L
    private val certificateType = CertificateType.NDFL
    private val certificateRequest = CertificateRequest(employeeNumber, certificateType)

    private val data = ByteArray(100)

    @Test
    fun process() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns data

        val certificate = certificateRequest.process(employeeNumber)

        assertEquals(certificateRequest, certificate.certificateRequest)
        assertEquals(employeeNumber, certificate.processedBy)
        assertEquals(data, certificate.data)

        unmockkObject(Scanner)
    }

    @Test
    fun getEmployeeNumber() {
        assertEquals(employeeNumber, certificateRequest.employeeNumber)
    }

    @Test
    fun getCertificateType() {
        assertEquals(certificateType, certificateRequest.certificateType)
    }
}