package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class CertificateRequestTest {

    private val scanData = Random.nextBytes(100)
    private val employeeNumber = Random.nextLong()

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns scanData
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun createCertificateRequest() {
        for (certificateType in CertificateType.values()) {
            val certificateRequest = CertificateRequest(Random.nextLong(), certificateType)
            val certificate = certificateRequest.process(employeeNumber)
            assertEquals(certificate.certificateRequest, certificateRequest)
            assertEquals(certificate.data, scanData)
            assertEquals(certificate.processedBy, employeeNumber)
        }
    }
}