package ru.sber.qa

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CertificateTest {
    @Test
    fun test() {
        val certificateRequest = mockk<CertificateRequest>()
        val processedBy = 1L
        val data = ByteArray(10)

        val cert = Certificate(certificateRequest, processedBy, data)

        assertTrue(cert.certificateRequest === certificateRequest)
        assertTrue(cert.processedBy === processedBy)
        assertTrue(cert.data === data)
    }
}

