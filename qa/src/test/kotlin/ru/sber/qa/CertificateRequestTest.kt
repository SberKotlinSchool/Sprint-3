package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals

internal class CertificateRequestTest {
    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun processPositive() {
        //given
        val data = byteArrayOf(10)
        val employeeNumber = 1L
        val request = CertificateRequest(employeeNumber, CertificateType.NDFL)
        val expectedResult = Certificate(request, employeeNumber, data)
        every { Scanner.getScanData() } returns data

        //when
        val actualResult = request.process(employeeNumber)

        //then
        verify(exactly = 1) { Scanner.getScanData() }
        assertEquals(expectedResult.certificateRequest, actualResult.certificateRequest)
        assertEquals(expectedResult.processedBy, actualResult.processedBy)
        assertEquals(expectedResult.data, actualResult.data)
    }


    @Test
    fun processNegative() {
        //given
        val employeeNumber = 1L
        val request = CertificateRequest(employeeNumber, CertificateType.NDFL)

        val expectedMessage = "Таймаут сканирования документа"
        every { Scanner.getScanData() }.throws(ScanTimeoutException())

        //then
        val exception = Assertions.assertThrows(ScanTimeoutException::class.java) { request.process(employeeNumber) }
        verify(exactly = 1) { Scanner.getScanData() }
        assertEquals(expectedMessage, exception.message)

    }

    @Test
    fun getEmployeeNumber() {
        //given
        val data = byteArrayOf(10)
        val employeeNumber = 1L
        val certificateType = CertificateType.LABOUR_BOOK
        val request = CertificateRequest(employeeNumber, certificateType)
        every { Scanner.getScanData() } returns data

        //when
        val actualResult = request.process(employeeNumber)

        //then
        verify(exactly = 1) { Scanner.getScanData() }
        assertEquals(employeeNumber, actualResult.certificateRequest.employeeNumber)
    }

    @ParameterizedTest
    @EnumSource(value = CertificateType::class, names = ["NDFL", "LABOUR_BOOK"])
    fun getCertificateType(certificateType: CertificateType) {
        //given
        val data = byteArrayOf(10)
        val employeeNumber = 1L
        val request = CertificateRequest(employeeNumber, certificateType)
        every { Scanner.getScanData() } returns data

        //when
        val actualResult = request.process(employeeNumber)

        //then
        verify(exactly = 1) { Scanner.getScanData() }
        assertEquals(certificateType, actualResult.certificateRequest.certificateType)
    }
}