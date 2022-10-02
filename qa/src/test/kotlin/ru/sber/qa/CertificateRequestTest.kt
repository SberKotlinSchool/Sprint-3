package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class CertificateRequestTest {

    @Test
    fun processTest() {
        mockkObject(Scanner)
        val expectedData = ByteArray(1)
        every { Scanner.getScanData() } returns expectedData

        for (type in CertificateType.values()) {
            val expectedHrEmployeeNumber = Random.nextLong()
            val expectedCertificateRequest = CertificateRequest(Random.nextLong(), type)
            val result = expectedCertificateRequest.process(expectedHrEmployeeNumber)

            assertEquals(result.certificateRequest, expectedCertificateRequest)
            assertEquals(result.processedBy, expectedHrEmployeeNumber)
            assertEquals(result.data, expectedData)
        }
    }
}