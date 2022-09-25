package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import ru.sber.qa.CertificateType.*

@ExtendWith(MockKExtension::class)
internal class CertificateRequestTest {

    @Test
    fun `getting correct certificate after processing`() {
        val someData = "someData".toByteArray()
        val hrEmpNumber = 2L
        val certRequest = CertificateRequest(1L, LABOUR_BOOK)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns someData
        certRequest.process(hrEmpNumber).run {
            assertEquals(certRequest, certificateRequest)
            assertEquals(hrEmpNumber, processedBy)
            assertEquals(someData, data)
        }
    }
}