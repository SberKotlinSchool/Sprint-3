package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CertificateRequestTest {

    @Test
    fun process() {
        // Given
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns byteArrayOf()
        val hrEmployeeNumber = 1L
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)

        // When
        val res = certificateRequest.process(hrEmployeeNumber)

        // Then
        assertEquals(res.processedBy, hrEmployeeNumber)
        assertEquals(res.certificateRequest, certificateRequest)
        assertArrayEquals(res.data, byteArrayOf())
    }
}