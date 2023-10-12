package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CertificateRequestTest {

    @Test
    fun process() {
        val hrEmployeeNumber = 1L
        val scanData = byteArrayOf(10)
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)
        mockkObject(Scanner)
        val expectedResult = Certificate(certificateRequest, hrEmployeeNumber, scanData)

        every { Scanner.getScanData() } returns scanData

        val result = certificateRequest.process(hrEmployeeNumber)
        println("${result.certificateRequest} ${result.data} ${result.processedBy}")
        println("${expectedResult.certificateRequest} ${expectedResult.data} ${expectedResult.processedBy}")

        assertEquals(expectedResult.certificateRequest, result.certificateRequest)
        assertEquals(expectedResult.processedBy, result.processedBy)
        assertEquals(expectedResult.data, result.data)

    }
}