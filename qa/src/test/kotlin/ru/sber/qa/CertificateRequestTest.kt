package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.assertEquals

internal class CertificateRequestTest {

    @BeforeEach
    fun init() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Scanner)
    }

    @Test
    fun processTest() {

        val scanData = Random.nextBytes(100)
        val employeeNumber = abs(Random.nextLong())

        every { Scanner.getScanData() } returns scanData

        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        val certificate = certificateRequest.process(employeeNumber)

        assertEquals(scanData, certificate.data)
        assertEquals(employeeNumber, certificate.processedBy)
        assertEquals(certificateRequest, certificate.certificateRequest)

    }
}