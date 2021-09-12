package ru.sber.qa

import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CertificateRequestTest {
    private val employeeNumber = 123L
    private val certificateType = CertificateType.NDFL
    private val scanData = byteArrayOf(0, 1, 2)

    private val certificateRequest = CertificateRequest(employeeNumber, certificateType)

    @Test
    fun process() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns scanData
        mockkConstructor(Certificate::class)

        val actualCertificate = certificateRequest.process(employeeNumber)

        val expectedCertificate = Certificate(certificateRequest, employeeNumber, scanData)
        assertEquals(expectedCertificate.certificateRequest, actualCertificate.certificateRequest)
        assertEquals(expectedCertificate.processedBy, actualCertificate.processedBy)
        assertEquals(expectedCertificate.data, actualCertificate.data)
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