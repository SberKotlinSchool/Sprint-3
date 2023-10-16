package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

internal class CertificateTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test()
    fun `test certificate properties`() {
        val certificateType = CertificateType.LABOUR_BOOK
        val employeeNumber = 12345L
        val hrEmployeeNumber = 54321L
        val certificateRequest = CertificateRequest(employeeNumber, certificateType)

        val scanData = byteArrayOf(1, 2, 3, 4, 5)

        every { Scanner.getScanData() } returns scanData

        val certificate = certificateRequest.process(hrEmployeeNumber)

        // Проверка свойств сертификата
        assert(certificate.certificateRequest == certificateRequest)
        assert(certificate.processedBy == hrEmployeeNumber)
        assert(certificate.data contentEquals scanData)
    }
}