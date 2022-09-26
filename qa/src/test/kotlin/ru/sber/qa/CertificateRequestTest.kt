package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CertificateRequestTest {
    @AfterAll
    fun unmock(){
        unmockkAll()
    }

    @Test
    fun `simple process test`() {
        val certificateRequest = CertificateRequest(1, CertificateType.NDFL)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(1)
        val actual = certificateRequest.process(2)

        assertNotNull(actual)
        assertEquals(certificateRequest, actual.certificateRequest)
    }
}