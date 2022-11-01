package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random


class CertificateRequestTest {

    private val employeeNumber : Long = 10
    private val certificateType = CertificateType.LABOUR_BOOK
    private val scanData = Random.nextBytes(100)
    private val certRequest = CertificateRequest(employeeNumber, certificateType)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns scanData
    }

    @Test
    fun testBase() {
        assertEquals(certificateType, certRequest.certificateType)
        assertEquals(employeeNumber, certRequest.employeeNumber)
    }

    @Test
    fun testProcess() {
        val cert = certRequest.process(employeeNumber)
        assertEquals(certRequest, cert.certificateRequest)
        assertEquals(employeeNumber, cert.processedBy)
        assertEquals(scanData, cert.data)
    }
}