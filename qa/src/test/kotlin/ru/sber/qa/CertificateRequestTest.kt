package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class CertificateRequestTest {

    private lateinit var certificateRequest: CertificateRequest
    private lateinit var byteArray: ByteArray

    @Test
    fun processShouldReturnCertificate() {
        mockkObject(Scanner)
        certificateRequest = CertificateRequest(123L, CertificateType.NDFL)
        byteArray = ByteArray(1)
        every { Scanner.getScanData() } returns byteArray
        val cert = certificateRequest.process(1L)
        verify { Scanner.getScanData() }
        assertNotNull(cert)
        assertEquals(1L, cert.processedBy)
        assertEquals(certificateRequest, cert.certificateRequest)
        assertEquals(byteArray, cert.data)
    }
}