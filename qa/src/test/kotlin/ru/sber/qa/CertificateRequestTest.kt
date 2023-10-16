package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CertificateRequestTest {

    private val employeeNumber: Long = 1L

    @Test
    fun process(){
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns byteArrayOf()
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        val res = certificateRequest.process(employeeNumber)

        assertEquals(res.processedBy, employeeNumber)
        assertEquals(res.certificateRequest, certificateRequest)
    }
}