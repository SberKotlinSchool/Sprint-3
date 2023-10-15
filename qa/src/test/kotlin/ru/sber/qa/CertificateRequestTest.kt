package ru.sber.qa

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals

internal class CertificateRequestTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @ParameterizedTest
    @EnumSource(CertificateType::class)
    fun `should process return CertificateRequest`(certificateType: CertificateType) {
        //given
        val hrEmployeeNumber = 1L
        val employeeNumber = 2L
        val certificateRequest = CertificateRequest(employeeNumber, certificateType)

        val data = byteArrayOf(100)
        every { Scanner.getScanData() } returns data

        //when
        val certificate = certificateRequest.process(hrEmployeeNumber)

        //then
        verify(exactly = 1) { Scanner.getScanData() }
        assertEquals(certificateRequest, certificate.certificateRequest)
        assertEquals(hrEmployeeNumber, certificate.processedBy)
        assertEquals(data, certificate.data)
    }
}