package src.main.kotlin.ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.sber.qa.*

internal class CertificateRequestTest {
    val mockedScandata = mockkObject(Scanner)
    private val hrEmployeeNumber = 99999L
    private val certRequestLabourBook = CertificateRequest(
        hrEmployeeNumber,
        certificateType = CertificateType.LABOUR_BOOK
    )
    private val certRequestNDFL = CertificateRequest(
        hrEmployeeNumber,
        certificateType = CertificateType.NDFL
    )

    @Test
    fun processForLabourBook() {
        every { Scanner.getScanData() } returns ByteArray(100)

        val certificate = certRequestLabourBook.process(hrEmployeeNumber)

        assertNotNull(certificate)
        assertTrue(certificate.certificateRequest === certRequestLabourBook)
        assertTrue(certificate.processedBy == hrEmployeeNumber)

    }

    @Test
    fun processForNDFL() {
        every { Scanner.getScanData() } returns ByteArray(100)

        val certificate = certRequestNDFL.process(hrEmployeeNumber)

        assertNotNull(certificate)
        assertTrue(certificate.certificateRequest === certRequestNDFL)
        assertTrue(certificate.processedBy == hrEmployeeNumber)

    }

    @Test
    fun processWithGetScanDataThrow() {
        every { Scanner.getScanData() } throws ScanTimeoutException()

        assertThrows(ScanTimeoutException::class.java) {
            certRequestNDFL.process(99999L)
        }
    }
}