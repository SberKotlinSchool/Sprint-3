package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import kotlin.random.Random

class CertificateRequestTest {
    private lateinit var data: ByteArray
    @BeforeEach
    fun beforeTests() {
        data = Random.nextBytes(100)
        mockkObject(Scanner)
    }

    @AfterEach
    fun afterTests() {
        unmockkObject(Scanner)
    }
    @Test
    @DisplayName("Проверка запроса сертификата")
    fun processTest() {
        every { Scanner.getScanData() } returns data
        val certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)

        // exp
        val expectedCertificate = Certificate(certificateRequest, 1L, data)

        // act
        val actualCertificate = certificateRequest.process(1L)

        assertEquals(expectedCertificate, actualCertificate)
        verify { Scanner.getScanData() }
    }
}