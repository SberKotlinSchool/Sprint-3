package ru.sber.qa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class CertificateRequestTest {
    private fun provideProcess() = Stream.of(
        Arguments.of(Certificate)
    )

    @ParameterizedTest
    @MethodSource("provideProcess")
    fun processTest(expected: Certificate, employeeNumber: Long, certificateType: CertificateType) {
        val certificateRequest = CertificateRequest(employeeNumber, certificateType)
        val actual = certificateRequest.process(employeeNumber)

        assertEquals(expected, actual)
    }

    fun
}