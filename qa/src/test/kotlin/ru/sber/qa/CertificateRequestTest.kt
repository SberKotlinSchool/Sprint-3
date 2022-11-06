package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.random.Random

internal class CertificateRequestTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            mockkObject(Scanner)
        }
    }

    @Nested
    inner class GivenScannerScanSuccessful {

        private val scanData = Random.nextBytes(150)

        @BeforeEach
        fun beforeEach() {
            every { Scanner.getScanData() } returns scanData
        }

        @ParameterizedTest
        @EnumSource(CertificateType::class)
        fun whenProcess_thenCertificate(certificateType: CertificateType) {
            val employeeNumber = Random.nextLong()
            val certificateRequest = CertificateRequest(employeeNumber, certificateType)
            val hrEmployerNumber = Random.nextLong()
            val certificate = certificateRequest.process(hrEmployerNumber)
            Assertions.assertEquals(hrEmployerNumber, certificate.processedBy)
            Assertions.assertEquals(certificateType, certificate.certificateRequest.certificateType)
            Assertions.assertEquals(employeeNumber, certificate.certificateRequest.employeeNumber)
            Assertions.assertEquals(scanData, certificate.data)
        }


    }

    @Nested
    inner class GivenScannerScanTimeout {

        @BeforeEach
        fun beforeEach() {
            every { Scanner.getScanData() } throws ScanTimeoutException()
        }

        @ParameterizedTest
        @EnumSource(CertificateType::class)
        fun whenProcess_thenCatchScanTimeoutException(certificateType: CertificateType) {
            val employeeNumber = Random.nextLong()
            val certificateRequest = CertificateRequest(employeeNumber, certificateType)
            val hrEmployerNumber = Random.nextLong()
            Assertions.assertThrows(ScanTimeoutException::class.java) {
                certificateRequest.process(hrEmployerNumber)
            }
        }


    }


}