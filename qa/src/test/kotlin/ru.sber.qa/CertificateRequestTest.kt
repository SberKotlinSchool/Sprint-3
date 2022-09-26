package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class CertificateRequestTest {

    @Test
    fun process() {
        mockkObject(Scanner)
        val employeeNumber = 1L
        val hrEmployeeNumber = 2L
        val randomArray = Random.nextBytes(100)

        every { Scanner.getScanData() } returns randomArray

        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        val certificate = certificateRequest.process(hrEmployeeNumber)

        assertEquals(certificate.processedBy, hrEmployeeNumber)
        assertEquals(certificate.data, randomArray)
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}