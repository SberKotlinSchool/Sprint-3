package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CertificateRequestTest{

    @BeforeEach
    internal fun setUp() {
        mockkObject(Scanner)
    }

    private val employeeNumber = 1L
    private val scanData = Random.nextBytes(100)
    private val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)

    @Test
    fun processTest() {
        every { Scanner.getScanData() } returns scanData
        val certificate = certificateRequest.process(employeeNumber)
        verify { certificateRequest.process(employeeNumber) }
        assertEquals(employeeNumber, certificate.processedBy)
        assertEquals(scanData, certificate.data)
        assertEquals(certificateRequest, certificate.certificateRequest)
    }



}