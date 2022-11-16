package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random


internal class CertificateRequestTest {
    val hrEmployeeNumber: Long = 111

    companion object {
        @JvmStatic
        fun getCertRequest() = Stream.of(
            Arguments.of(
                CertificateRequest(employeeNumber = 111, CertificateType.NDFL),
                Arguments.of(
                    CertificateRequest(employeeNumber = 222, CertificateType.LABOUR_BOOK)
                )
            )
        )
    }

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData(Constants.SCAN_TIMEOUT_THRESHOLD) } returns Random.nextBytes(1)
    }

    @ParameterizedTest
    @MethodSource("getCertRequest")
    fun processReturnsCertificateTest(certRequest: CertificateRequest) {
        val certificate = certRequest.process(hrEmployeeNumber)
        assertEquals(hrEmployeeNumber, certificate.processedBy)
        assertEquals(certRequest, certificate.certificateRequest)
    }
}