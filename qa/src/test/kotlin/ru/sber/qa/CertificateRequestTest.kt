package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

private const val EMPLOYEE_NUMBER = 1L
private const val HR_EMPLOYEE_NUMBER = 2L

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CertificateRequestTest {

    @BeforeAll
    fun beforeAll() {
        mockkObject(Scanner)
    }

    @Test
    fun testProcess() {
        val byteArray = ByteArray(1)
        every { Scanner.getScanData() } returns byteArray
        val certificateRequest = CertificateRequest(EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK)
        val certificate = certificateRequest.process(HR_EMPLOYEE_NUMBER)
        assertEquals(certificateRequest, certificate.certificateRequest)
        assertEquals(HR_EMPLOYEE_NUMBER, certificate.processedBy)
        assertEquals(byteArray, certificate.data)
    }

}