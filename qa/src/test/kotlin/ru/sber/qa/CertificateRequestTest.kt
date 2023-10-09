package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CertificateRequestTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @Test
    fun process() {
        val processedBy = 1L
        val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)
        val data = byteArrayOf(1)
        every { Scanner.getScanData() } returns data

        val actual = certificateRequest.process(processedBy)

        verify { Scanner.getScanData() }
        assertEquals(certificateRequest, actual.certificateRequest)
        assertEquals(processedBy, actual.processedBy)
        assertEquals(data, actual.data)
    }
}