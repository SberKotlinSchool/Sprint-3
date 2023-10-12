package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CertificateRequestTest {

    @BeforeEach
    fun beforeTest() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun afterTest() {
        unmockkObject(Scanner)
    }

    @Test
    fun process() {
        val byteArray = byteArrayOf(7)
        every { Scanner.getScanData() } returns byteArray
        val employeeNumber = 7L
        val hrEmployeeNumber = 8L
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)

        val certificate = certificateRequest.process(hrEmployeeNumber)

        verify(exactly = 1) { Scanner.getScanData() }
        assertEquals(certificateRequest, certificate.certificateRequest)
        assertEquals(hrEmployeeNumber, certificate.processedBy)
        assertEquals(byteArray, certificate.data)
    }
}