package ru.sber.qa

import io.mockk.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue

import kotlin.random.Random
class CertificateRequestTest {

    private val scanData = Random.nextBytes(100)
    private val HR_EMPLOYEE_NUMBER = Random.nextLong()
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
        for ( certificateType in CertificateType.values() ){
            val certificateRequest = CertificateRequest( Random.nextLong(), certificateType )
            val certificate = certificateRequest.process( HR_EMPLOYEE_NUMBER )
            assertEquals(certificate.certificateRequest, certificateRequest)
            assertEquals(certificate.data, scanData)
            assertEquals(certificate.processedBy, HR_EMPLOYEE_NUMBER)
        }
    }
}