package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class CertificateRequestTest {

    private val employeeNumber = 1L
    private val certificateType = CertificateType.NDFL
    private var certificateRequest = CertificateRequest(employeeNumber, certificateType)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun process() {
        val data = Random.nextBytes(100)

        every { Scanner.getScanData() } returns data

        val certificate = certificateRequest.process(employeeNumber)

        assertEquals(certificate.certificateRequest, certificateRequest)
        assertEquals(certificate.processedBy, employeeNumber)
        assertEquals(certificate.data, data)

        verify(exactly = 1) { Scanner.getScanData() }
    }
}