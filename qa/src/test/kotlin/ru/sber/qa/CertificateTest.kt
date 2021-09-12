package ru.sber.qa

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CertificateTest {
    private val request = mockk<CertificateRequest>()
    private val processedBy = 1L
    private val data = byteArrayOf(0, 1, 2)

    private val certificate: Certificate = Certificate(request, processedBy, data)

    @Test
    fun getCertificateRequest() {
        assertEquals(request, certificate.certificateRequest)
    }

    @Test
    fun getProcessedBy() {
        assertEquals(processedBy, certificate.processedBy)
    }

    @Test
    fun getData() {
        assertEquals(data, certificate.data)
    }
}