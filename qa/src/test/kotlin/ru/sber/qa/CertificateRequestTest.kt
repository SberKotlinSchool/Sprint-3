package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CertificateRequestTest {

    val employeeNumber = 11L

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun processTest() {
        val scanData = ByteArray(1)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns scanData

        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)

        val result = certificateRequest.process(employeeNumber)
        assertEquals(employeeNumber, result.processedBy)
        assertEquals(certificateRequest, result.certificateRequest)
        assertEquals(scanData, result.data)
    }
}