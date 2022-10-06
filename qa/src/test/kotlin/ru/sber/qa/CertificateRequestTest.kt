package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.assertFailsWith

internal class CertificateRequestTest {

    @BeforeEach
    fun init() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Random)
    }

    @Test
    fun process() {
        val scanData = Random.nextBytes(abs(Random.nextInt(1, 128)))
        val employeeNumber = abs(Random.nextLong())
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)

        every { Random.nextLong(5000L, 15000L) } returnsMany listOf(5000L, 14999L)
        every { Random.nextBytes(100) } returns  scanData

        val certificate = certificateRequest.process(employeeNumber)

        assertEquals(certificate.processedBy, employeeNumber)
        assertEquals(certificate.certificateRequest, certificateRequest)
        assertEquals(certificate.data, scanData)

        assertFailsWith<ScanTimeoutException> { certificateRequest.process(employeeNumber) }
    }
}