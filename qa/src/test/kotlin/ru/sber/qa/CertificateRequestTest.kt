package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CertificateRequestTest {
    @Test
    fun process() {
        val hrEmployeeNumber = 1L
        val scanData = byteArrayOf(10)
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)
        val expectedResult = Certificate(certificateRequest, hrEmployeeNumber, scanData)

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns scanData

        val result = certificateRequest.process(hrEmployeeNumber)

        assertEquals(expectedResult.certificateRequest, result.certificateRequest)
        assertEquals(expectedResult.processedBy, result.processedBy)
        assertEquals(expectedResult.data, result.data)
    }

}