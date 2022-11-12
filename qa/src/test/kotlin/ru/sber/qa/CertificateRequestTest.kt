package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CertificateRequestTest {

    private val hrEmployeeNumber: Long = 1L
    private val hrScannerData = ByteArray(100)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns hrScannerData
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun processCertificateRequest() {
        for (type in CertificateType.values()) {
            val expectedRequest = CertificateRequest(hrEmployeeNumber,type)
            val result = expectedRequest.process(hrEmployeeNumber)

            assertEquals(expectedRequest, result.certificateRequest) // сверяем запрос
            assertEquals(hrEmployeeNumber, result.processedBy) // сверяем табельный номер
            assertEquals(hrScannerData, result.data) // сверяем скан документа

        }
    }
}