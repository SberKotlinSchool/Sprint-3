package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CertificateRequestTest {

    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    private fun init() {
        certificateRequest = CertificateRequest(123, CertificateType.NDFL)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns "111".toByteArray()
    }

    @Test
    fun processTest() {
        val certificate = certificateRequest.process(321)
        assertEquals(Certificate(certificateRequest, 321, "111".toByteArray()), certificate)
    }

    @Test
    fun getEmployeeNumberTest() {
        assertEquals(123, certificateRequest.employeeNumber)
    }

    @Test
    fun getCertificateTypeTest() {
        assertEquals(certificateRequest.certificateType, CertificateType.NDFL)
    }
}