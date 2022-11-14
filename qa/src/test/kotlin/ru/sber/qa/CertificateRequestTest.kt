package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CertificateRequestTest {
    private lateinit var certificateRequest: CertificateRequest
    private lateinit var byteArray: ByteArray

    private val employeeNumber = 111L
    private val hrEmployeeNumber = 222L

    @BeforeEach
    fun beforeTests() {
        mockkObject(Scanner)
        certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
        byteArray = ByteArray(1)
    }

    @Test
    fun `getters should return values of initialized fields`() {
        assertEquals(employeeNumber, certificateRequest.employeeNumber)
        assertEquals(CertificateType.LABOUR_BOOK, certificateRequest.certificateType)
    }

    @Test
    fun `process() should return Certificate`() {
        every { Scanner.getScanData() } returns byteArray

        val certificate = certificateRequest.process(hrEmployeeNumber)

        verify { Scanner.getScanData() }

        assertNotNull(certificate)
        assertEquals(expected = certificateRequest, actual = certificate.certificateRequest)
        assertEquals(expected = byteArray, actual = certificate.data)
        assertEquals(expected = hrEmployeeNumber, actual = certificate.processedBy)
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
    }
}