package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class CertificateRequestTest {

    @BeforeEach
    fun mockkScanner(){
        mockkObject(Scanner)
    }

    @AfterEach
    fun unMock(){
        unmockkAll()
    }

    @Test
    fun process() {

        val scanData = ByteArray(100)

        every { Scanner.getScanData() } returns scanData

        val certificateRequest = CertificateRequest(10, CertificateType.LABOUR_BOOK )
        val hrEmployeeNumber = 555L
        val certificate = certificateRequest.process(hrEmployeeNumber)

        assertEquals(certificateRequest, certificate.certificateRequest)
        assertEquals(hrEmployeeNumber, certificate.processedBy)
        assertEquals(scanData, certificate.data )

    }
}