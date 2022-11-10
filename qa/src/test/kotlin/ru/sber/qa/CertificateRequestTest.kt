package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.random.Random

internal class CertificateRequestTest {

    private val scanData = Random.nextBytes(150)

    @BeforeEach
    fun beforeEach() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun afterEach() {
        unmockkAll()
    }

    @ParameterizedTest
    @EnumSource(CertificateType::class)
    fun whenProcess_thenCertificate(certificateType: CertificateType) {
        every { Scanner.getScanData() } returns scanData
        val employeeNumber = Random.nextLong()
        val certificateRequest = CertificateRequest(employeeNumber, certificateType)
        val hrEmployerNumber = Random.nextLong()
        val certificate = certificateRequest.process(hrEmployerNumber)
        Assertions.assertEquals(hrEmployerNumber, certificate.processedBy)
        Assertions.assertEquals(certificateType, certificate.certificateRequest.certificateType)
        Assertions.assertEquals(employeeNumber, certificate.certificateRequest.employeeNumber)
        Assertions.assertEquals(scanData, certificate.data)
    }


    @ParameterizedTest
    @EnumSource(CertificateType::class)
    fun whenProcess_thenCatchScanTimeoutException(certificateType: CertificateType) {
        every { Scanner.getScanData() } throws ScanTimeoutException()
        val employeeNumber = Random.nextLong()
        val certificateRequest = CertificateRequest(employeeNumber, certificateType)
        val hrEmployerNumber = Random.nextLong()
        Assertions.assertThrows(ScanTimeoutException::class.java) {
            certificateRequest.process(hrEmployerNumber)
        }
    }

}