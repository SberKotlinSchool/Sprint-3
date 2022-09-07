package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CertificateRequestTest {

    @Test
    fun process() {
        val certificateRequest = CertificateRequest(12, CertificateType.LABOUR_BOOK)
        mockkObject(Scanner)

        val byteArray = "Тест".toByteArray()
        every { Scanner.getScanData() } returns byteArray

        val result = certificateRequest.process(10)

        assertEquals(byteArray, result.data)
        assertEquals(certificateRequest, result.certificateRequest)
        assertEquals(10, result.processedBy)
    }

    @AfterEach
    fun after() {
        unmockkAll()
    }
}