package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CertificateRequestTest {

    @Test
    fun process() {
        val hrEmployeeNumber = 11L
        val certReq = CertificateRequest(10L, CertificateType.NDFL)
        val bytes = ByteArray(10)

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns bytes

        val cert = certReq.process(hrEmployeeNumber)

        assertEquals(hrEmployeeNumber, cert.processedBy)
        assertEquals(certReq, cert.certificateRequest)
        assertEquals(bytes, cert.data)
    }

    @AfterEach
    fun clean() {
        unmockkObject(Scanner)
    }
}