package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CertificateRequestTest {
    val expectedScanData = ByteArray(1)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns expectedScanData
    }

    @Test
    fun processTest() {


        for (type in CertificateType.values()) {
            val expectedHrEmployeeNumber = Random.nextLong()
            val expectedCertificateRequest = CertificateRequest(Random.nextLong(), type)
            val certificate = expectedCertificateRequest.process(expectedHrEmployeeNumber)

            assertEquals(certificate.certificateRequest, expectedCertificateRequest)
            assertEquals(certificate.processedBy, expectedHrEmployeeNumber)
            assertEquals(certificate.data, expectedScanData)
        }
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}
