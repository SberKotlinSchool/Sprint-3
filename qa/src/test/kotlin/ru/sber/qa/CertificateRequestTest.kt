package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CertificateRequestTest {

    @Test
    fun process() {
        //give
        val certificateRequest = CertificateRequest(1, CertificateType.NDFL)
        mockkObject(Scanner)

        //when
        val data = ByteArray(1)
        every { Scanner.getScanData() } returns data
        val certificate = certificateRequest.process(1)

        //then
        assertEquals(certificate.certificateRequest, certificateRequest)
        assertEquals(certificate.processedBy, 1)
        assertEquals(certificate.data, data)
        verify(exactly = 1) { Scanner.getScanData() }
    }
}