package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CertificateRequestTest {
    @Test
    fun process() {
        val certificateType = CertificateType.LABOUR_BOOK
        val employeeNumber = 1L
        val hrEmployeeNumber = 2L

        mockkObject(Scanner)
        val byteArray = "Scanner result".toByteArray()
        every { Scanner.getScanData() } returns byteArray

        val certificateRequest = CertificateRequest(employeeNumber, certificateType)
        val result = certificateRequest.process(hrEmployeeNumber)

        assertEquals(certificateRequest, result.certificateRequest)
        assertEquals(hrEmployeeNumber, result.processedBy)
        assertEquals(byteArray, result.data)
    }
}