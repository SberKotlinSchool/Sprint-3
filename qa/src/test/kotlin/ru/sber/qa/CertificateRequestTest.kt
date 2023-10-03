package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class CertificateRequestTest {
    private val cert = CertificateRequest(5L, CertificateType.NDFL)
    private val scanData = ByteArray(2)
    private val certificateToReturn = Certificate(cert, 5L, scanData)

    @Test
    fun processTest() {
        mockkObject(Scanner)

        every { Scanner.getScanData() } returns scanData

        val processedCert = cert.process(5L)

        assertEquals(certificateToReturn.certificateRequest, processedCert.certificateRequest)
        assertEquals(certificateToReturn.processedBy, processedCert.processedBy)
        assertEquals(certificateToReturn.data, processedCert.data)

        verify(exactly = 1) { Scanner.getScanData() }

        unmockkObject(Scanner)
    }
}