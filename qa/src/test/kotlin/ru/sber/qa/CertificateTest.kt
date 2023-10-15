package ru.sber.qa

import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CertificateTest {

    private val certificateRequest = mockk<CertificateRequest>()
    private val data = byteArrayOf(127)
    private val certificate = Certificate(certificateRequest = certificateRequest, processedBy = 1, data = data)

    @Test
    fun getCertificateRequest() {
        assertNotNull(certificate.certificateRequest)
    }

    @Test
    fun getProcessedBy() {
        assertEquals(1, certificate.processedBy)
    }

    @Test
    fun getData() {
        assertEquals(data, certificate.data)
    }
}