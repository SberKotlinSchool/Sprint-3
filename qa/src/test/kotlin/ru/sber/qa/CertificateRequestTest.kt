package ru.sber.qa

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CertificateRequestTest {

    @Test
    fun `process returns a Certificate`() {
        val hrEmployeeNumber = 123L
        val certificateType = CertificateType.NDFL
        val certificateRequest = CertificateRequest(456L, certificateType)

        val scanData = ByteArray(100)
        every { Scanner.getScanData() } returns scanData

        val certificate = certificateRequest.process(hrEmployeeNumber)

        assertEquals(certificateRequest, certificate.certificateRequest)
        assertEquals(hrEmployeeNumber, certificate.processedBy)
        assertEquals(scanData, certificate.data)

        verify(exactly = 1) { Scanner.getScanData() }
    }
}
