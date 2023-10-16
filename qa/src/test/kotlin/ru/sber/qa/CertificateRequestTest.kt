package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class CertificateRequestTest {
    companion object {
        private val TEST_SCANNER_RESULT = byteArrayOf()
        private const val TEST_EMPLOYEE_NUMBER = 777L
        private const val TEST_HR_EMPLOYEE_NUMBER = 666L
        private val TEST_CERTIFICATE_TYPE = CertificateType.LABOUR_BOOK

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            mockkObject(Scanner)
            every { Scanner.getScanData() } returns TEST_SCANNER_RESULT
        }

        @JvmStatic
        @AfterAll
        fun afterTest() {
            unmockkAll()
        }
    }

    @Test
    fun testProcess() {
        val testCertificateRequest = CertificateRequest(TEST_EMPLOYEE_NUMBER, TEST_CERTIFICATE_TYPE)
        val result = testCertificateRequest.process(TEST_HR_EMPLOYEE_NUMBER)
        assertEquals(TEST_HR_EMPLOYEE_NUMBER, result.processedBy)
        assertEquals(TEST_SCANNER_RESULT, result.data)
        assertEquals(testCertificateRequest, result.certificateRequest)
        assertEquals(testCertificateRequest.certificateType, TEST_CERTIFICATE_TYPE)
        assertEquals(testCertificateRequest.employeeNumber, TEST_EMPLOYEE_NUMBER)
    }
}