package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CertificateRequestTest {
    private val request = CertificateRequest(1L, CertificateType.NDFL)

    @BeforeEach
    fun setUp(){
        mockkObject(Scanner)
    }

    @Test
    fun processSuccess() {
        every { Scanner.getScanData() } returns byteArrayOf(12, 13, 14)
        val expected = Certificate(request, 2L, Scanner.getScanData())
        val actual = request.process(2L)

        assertEquals(expected.certificateRequest,actual.certificateRequest)
        assertEquals(expected.processedBy, actual.processedBy)
        assertEquals(expected.data,actual.data)
    }

    @Test
    fun throwTimeoutException(){
        every { Scanner.getScanData() } throws ScanTimeoutException()
        assertThrows<ScanTimeoutException> { request.process(2L) }
    }

}