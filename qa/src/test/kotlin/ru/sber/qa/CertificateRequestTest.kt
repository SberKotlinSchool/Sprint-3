package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class CertificateRequestTest {

    private val data = byteArrayOf(1)
    private val sut = CertificateRequest(1L, CertificateType.LABOUR_BOOK)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns data
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun shouldProcess() {
        val certificate = sut.process(1L)
        assertEquals(data, certificate.data)
        assertEquals(1L, certificate.processedBy)
        assertEquals(sut.employeeNumber, certificate.certificateRequest.employeeNumber)
        assertEquals(sut.certificateType , certificate.certificateRequest.certificateType)
    }

    @Test
    fun shouldGetEmployeeNumber() {
        assertEquals(1L, sut.employeeNumber)
    }

    @Test
    fun shouldGetCertificateType() {
        assertEquals(CertificateType.LABOUR_BOOK, sut.certificateType)
    }
}