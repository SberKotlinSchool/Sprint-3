package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CertificateRequestTest {
    val hrEmployeeNumber = 1L
    val data = Random.nextBytes(100)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns data
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `process returns correct data`() {
        val certificateType = CertificateType.values().random()
        val certificateRequest = CertificateRequest(employeeNumber = 2L, certificateType = certificateType)
        val certificate = certificateRequest.process(hrEmployeeNumber)
        verify(exactly = 1) { Scanner.getScanData() }
        assertEquals(certificate.data, data)
        assertEquals(certificate.processedBy, hrEmployeeNumber)
        assertEquals(certificate.certificateRequest, certificateRequest)
    }
}