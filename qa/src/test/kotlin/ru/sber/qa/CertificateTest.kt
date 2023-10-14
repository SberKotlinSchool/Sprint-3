package ru.sber.qa

import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CertificateTest {

    private val certificateRequest = mockk<CertificateRequest>()
    private val data = byteArrayOf(1)
    private val sut = Certificate(certificateRequest, 1L, data)

    @Test
    fun shouldGetCertificateRequest() {
        assertNotNull(sut.certificateRequest)
    }

    @Test
    fun shouldGetProcessedBy() {
        assertEquals(1L, sut.processedBy)
    }

    @Test
    fun shouldGetData() {
        assertEquals(data, sut.data)
    }
}