package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class CertificateRequestTest {

    private val hrEmployeeNumber = 999L;
    private val certificateType = CertificateType.LABOUR_BOOK
    private var cr: CertificateRequest = CertificateRequest(hrEmployeeNumber, certificateType)

    @Test
    fun process() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns byteArrayOf()
        val process = cr.process(hrEmployeeNumber);

        Assertions.assertEquals(process.processedBy, hrEmployeeNumber);
        Assertions.assertEquals(process.certificateRequest, cr);
    }

    @Test
    fun getEmployeeNumber() {
        Assertions.assertEquals(hrEmployeeNumber, cr.employeeNumber);
    }

    @Test
    fun getCertificateType() {
        Assertions.assertEquals(certificateType, cr.certificateType);
    }
}