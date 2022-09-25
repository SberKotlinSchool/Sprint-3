package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CertificateRequestTest {
    private val empNo = 1L
    private val hrEmpNo = 2L
    private val type = CertificateType.LABOUR_BOOK

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        val scanData = ByteArray(1)
        every { Scanner.getScanData() } returns scanData
    }

    @Test
    fun test() {
        val request = CertificateRequest(empNo, type)
        assertTrue(request.employeeNumber === empNo)
        assertTrue(request.certificateType===type)

        val cert = request.process(hrEmpNo)
        assertEquals(cert.certificateRequest, request)
        assertEquals(cert.processedBy, hrEmpNo)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Scanner)
    }


}