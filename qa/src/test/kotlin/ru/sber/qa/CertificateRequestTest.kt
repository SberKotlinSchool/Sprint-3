package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CertificateRequestTest {

    private val employeeNumber: Long = 1111
    private val certificateType: CertificateType = CertificateType.NDFL
    private val expectedData = ByteArray(100)

    private val certificateRequest = CertificateRequest(employeeNumber, certificateType)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)

        every { Scanner.getScanData() } returns expectedData
    }

    @AfterEach
    fun closeUp() {
        unmockkAll()
    }

    @Test
    fun shouldReturnCertificate() {
        val actualCertificate = certificateRequest.process(employeeNumber)

        assertNotNull(actualCertificate)
        assertEquals(certificateRequest, actualCertificate.certificateRequest)
        assertEquals(expectedData, actualCertificate.data)
        assertEquals(employeeNumber, actualCertificate.processedBy)
    }
}