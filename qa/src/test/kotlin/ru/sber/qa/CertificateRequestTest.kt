package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CertificateRequestTest {
    private val hrEmployeeNumber = 1L

    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    fun setUp() {
        certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(1)
    }
    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
    @Test
    fun `processedBy valid`() {
        val certificate = certificateRequest.process(hrEmployeeNumber)
        assertEquals(certificate.processedBy, hrEmployeeNumber)
    }

    @Test
    fun `certificateRequest valid`() {
        val certificate = certificateRequest.process(hrEmployeeNumber)
        assertEquals(certificateRequest, certificate.certificateRequest)
    }
}