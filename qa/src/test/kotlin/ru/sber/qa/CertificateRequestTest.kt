package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.Before
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random


internal class CertificateRequestTest {
    val req = mockkClass(CertificateRequest::class)

    val hrEmployeeNumber: Long = 111
    val cert = Certificate(req, hrEmployeeNumber, Random.nextBytes(1))

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns Random.nextBytes(1)

        every { req.employeeNumber } returns 1
        every { req.certificateType } returns CertificateType.NDFL
        every { req.process(hrEmployeeNumber) } returns cert
    }

    @Test
    fun process() {
        val process = req.process(hrEmployeeNumber)
        assertEquals(hrEmployeeNumber, process.processedBy)
        assertEquals(cert.certificateRequest, process.certificateRequest)
    }
}