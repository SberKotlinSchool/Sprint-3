package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class CertificateRequestTest {
    companion object {
        private const val HR_EMPLOYEE_NUMBER = 1234L
        private val request = CertificateRequest(HR_EMPLOYEE_NUMBER, CertificateType.LABOUR_BOOK)
    }

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @Test
    fun processScanTimeoutException() {
        every { Scanner.getScanData() } throws ScanTimeoutException()
        assertThrows(ScanTimeoutException::class.java) { request.process(HR_EMPLOYEE_NUMBER) }
    }

    @Test
    fun processSuccessfully() {
        val scanData = Random.nextBytes(100)
        every { Scanner.getScanData() } returns scanData
        assertEquals(scanData, request.process(HR_EMPLOYEE_NUMBER).data)
    }

    @Test
    fun getEmployeeNumber() {
        assertEquals(HR_EMPLOYEE_NUMBER, request.employeeNumber)
    }

    @Test
    fun getCertificateType() {
        assertEquals(CertificateType.LABOUR_BOOK, request.certificateType)
    }
}