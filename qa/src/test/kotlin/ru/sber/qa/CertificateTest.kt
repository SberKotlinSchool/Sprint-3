package ru.sber.qa

import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import kotlin.random.Random


class CertificateTest {

    private val hrEmployeeNumber: Long = 10
    private val scanData = Random.nextBytes(100)
    private val certRequest = mockk<CertificateRequest>()
    private val cert = Certificate(certRequest, hrEmployeeNumber, scanData)

    @Test
    fun testBase() {
        Assertions.assertAll("cert",
            Executable { assertEquals(hrEmployeeNumber, cert.processedBy) },
            Executable { assertEquals(certRequest, cert.certificateRequest) },
            Executable { assertEquals(scanData, cert.data) }
        )
    }
}