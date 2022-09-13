package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("CertificateRequest class test cases")
internal class CertificateRequestTest {

    private val expectedDataSize: Int = 100

    @Test
    fun `CertificateRequest process() test with LABOUR_BOOK type with mock without exception`() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(100)
        val employeeNumber: Long = 252
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
        val certificateWithLabourBook = certificateRequest.process(employeeNumber)
        assertAll(
            "Certificate",
            { assertNotNull(certificateWithLabourBook, "Certificate not null check") },
            { assertEquals(certificateWithLabourBook.certificateRequest, certificateRequest,"Certificate request check")},
            { assertEquals(certificateWithLabourBook.processedBy, employeeNumber, "Certificate processBy check") },
            { assertEquals(certificateWithLabourBook.data.size, expectedDataSize, "Certificate data size check") }
        )

    }

    @Test
    fun `CertificateRequest process() test with LABOUR_BOOK type with mock and timeout exception`() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } throws ScanTimeoutException()
        val employeeNumber: Long = 252
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
        assertThrows(ScanTimeoutException::class.java) {
            certificateRequest.process(employeeNumber)
        }
    }

    @Test
    fun `CertificateRequest process() test with NFDL type with mock without exception`() {

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(100)

        val employeeNumber: Long = 251
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        val certificateWithNfl = certificateRequest.process(employeeNumber)

        assertAll(
            { assertNotNull(certificateWithNfl, "Certificate not null check") },
            { assertEquals(certificateWithNfl.certificateRequest, certificateRequest, "Certificate request check") },
            { assertEquals(certificateWithNfl.processedBy, employeeNumber, "Certificate processBy check") },
            { assertEquals(certificateWithNfl.data.size, expectedDataSize, "Certificate data size check") }
        )
    }

    @Test
    fun `CertificateRequest process() test with NFDL type with mock and timeout exception`() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } throws ScanTimeoutException()
        val employeeNumber: Long = 252
        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
        assertThrows(ScanTimeoutException::class.java) {
            certificateRequest.process(employeeNumber)
        }
    }

//@deprecated
//    @Test
//    fun `CertificateRequest process() test with LABOUR_BOOK type`() {
//
//        val employeeNumber: Long = 252
//        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
//
//        try {
//
//            val certificateWithLabourBook = certificateRequest.process(employeeNumber)
//            //with assertAll
//            assertAll(
//                "Certificate",
//                { assertNotNull(certificateWithLabourBook, "Certificate not null check") },
//                { assertEquals(certificateWithLabourBook.certificateRequest, certificateRequest,"Certificate request check")},
//                { assertEquals(certificateWithLabourBook.processedBy, employeeNumber, "Certificate processBy check") },
//                { assertEquals(certificateWithLabourBook.data.size, expectedDataSize, "Certificate data size check") }
//            )
//
//
//        } catch (e: ScanTimeoutException) {
//            assertEquals(e.message, "Таймаут сканирования документа", "Exception check")
//        }
//
//    }

//    @Test
//    fun `CertificateRequest process() test with NFDL type`() {
//
//        val employeeNumber: Long = 251
//        val certificateRequest = CertificateRequest(employeeNumber, CertificateType.NDFL)
//
//        try {
//
//            val certificateWithNfl = certificateRequest.process(employeeNumber)
//            //just asserts
//            assertNotNull(certificateWithNfl, "Certificate not null check")
//            assertEquals(certificateWithNfl.certificateRequest, certificateRequest, "Certificate request check")
//            assertEquals(certificateWithNfl.processedBy, employeeNumber, "Certificate processBy check")
//            assertEquals(certificateWithNfl.data.size, expectedDataSize, "Certificate data size check")
//
//        } catch (e: ScanTimeoutException) {
//            assertEquals(e.message, "Таймаут сканирования документа", "Exception check")
//        }
//
//    }
}