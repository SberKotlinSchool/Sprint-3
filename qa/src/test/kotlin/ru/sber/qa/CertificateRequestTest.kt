package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CertificateRequestTest {
    private val employeeNumber = 123L
    private val certificateType = CertificateType.LABOUR_BOOK

    private val certificateRequest = CertificateRequest(
        employeeNumber = employeeNumber,
        certificateType = certificateType
    )

    @BeforeEach
    fun init() {
        mockkObject(Scanner)
    }

    @Test
    fun processTest() {
        val hrEmployeeNumber = 1234L
        val resultByteArray = ByteArray(1)

        every { Scanner.getScanData() } returns resultByteArray

        val result = certificateRequest.process(hrEmployeeNumber)

        assertEquals(certificateRequest, result.certificateRequest)
        assertEquals(hrEmployeeNumber, result.processedBy)
        assertEquals(resultByteArray.size, result.data.size)
    }

    @Test
    fun getEmployeeNumberTest() {
        assertEquals(employeeNumber, certificateRequest.employeeNumber)
    }
    @Test
    fun getCertificateTypeTest() {
        assertEquals(certificateType, certificateRequest.certificateType)
    }
}