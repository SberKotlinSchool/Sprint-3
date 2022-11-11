package ru.sber.qa.model

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.sber.qa.component.Scanner
import ru.sber.qa.getTestByteArray

internal class CertificateRequestTest {

    @Test
    @DisplayName("processTest")
    fun processTest() {
        val testByteArray = getTestByteArray()
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns testByteArray

        val emplNumber = 1L
        val hrNumber = 10L

        val certificateRequest = CertificateRequest(emplNumber, CertificateType.LABOUR_BOOK)
        val actual = certificateRequest.process(hrNumber)
        verify { Scanner.getScanData() }

        assertNotNull(actual)
        assertEquals(certificateRequest.certificateType, actual.certificateRequest.certificateType)
        assertEquals(emplNumber, actual.certificateRequest.employeeNumber)
        assertEquals(hrNumber, actual.processedBy)
        assertEquals(testByteArray, actual.data)
    }
}