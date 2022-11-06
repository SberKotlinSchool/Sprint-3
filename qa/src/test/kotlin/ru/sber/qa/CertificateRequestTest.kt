package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class CertificateRequestTest {

    private lateinit var certificateRequest: CertificateRequest

    private val employeeNumber = 100L
    private val hrEmployeeNumber = 200L

    @BeforeEach
    fun setUp() {
        certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(1)
    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }

    @Test
    fun processTest()
    {
        val certificate = certificateRequest.process(hrEmployeeNumber)
        assertAll("certificate data",
            { assertNotNull(certificate) },
            { assertEquals(certificate.processedBy, hrEmployeeNumber) },
            { assertEquals(certificate.certificateRequest, certificateRequest) }
        )
    }

}