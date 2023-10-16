package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CertificateRequestTest {

    @Test
    fun processTest() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns byteArrayOf(0x2)

        val certificateRequest = CertificateRequest(111, CertificateType.NDFL)
        val res = certificateRequest.process(222)

        assertEquals(res.processedBy, 222)
        assertEquals(res.certificateRequest, certificateRequest)
        assertArrayEquals(res.data, byteArrayOf(0x2))

        unmockkAll()
    }

}
