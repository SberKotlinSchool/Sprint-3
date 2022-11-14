package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import ru.sber.qa.CertificateType.NDFL
import ru.sber.qa.CertificateType.LABOUR_BOOK
import kotlin.random.Random
import kotlin.test.assertEquals

internal class CertificateRequestTest {

    val TEST_HR_EMPLOYEE_NUMBER = 2193L

    @Test
    fun processTest_NDFLProcessWithTestHREmployeeNumber_returnsCertificateProcessedByTestHREmployeeNumber() {
        //setup
        mockkObject(Scanner)
        val testScanData = Random.nextBytes(100)
        every { Scanner.getScanData() } returns testScanData
        val certificateRequest = CertificateRequest( employeeNumber = Random.nextLong() , certificateType = NDFL)
        val certificate = certificateRequest.process(TEST_HR_EMPLOYEE_NUMBER)

        //assert
        assertEquals(certificate.data, testScanData)
        assertEquals(certificate.processedBy, TEST_HR_EMPLOYEE_NUMBER)
        assertEquals(certificate.certificateRequest, certificateRequest)

    }

    @Test
    fun processTest_LabourBookProcessWithTestHREmployeeNumber_returnsCertificateProcessedByTestHREmployeeNumber() {
        //setup
        mockkObject(Scanner)
        val testScanData = Random.nextBytes(100)
        every { Scanner.getScanData() } returns testScanData
        val certificateRequest = CertificateRequest( employeeNumber = Random.nextLong() , certificateType = LABOUR_BOOK)
        val certificate = certificateRequest.process(TEST_HR_EMPLOYEE_NUMBER)

        //assert
        assertEquals(certificate.data, testScanData)
        assertEquals(certificate.processedBy, TEST_HR_EMPLOYEE_NUMBER)
        assertEquals(certificate.certificateRequest, certificateRequest)

    }
/*  Мне кажется, что тесты должны быть раздельны по типу сертификата, а если добавится новый, то следует писать новый тест
    Но на всякий случай оставлю тест, в котором все типы сертификатов пробегаются по циклу for (certificateType in CertificateType.values())
    @Test
    fun processTest_ProcessWithTestHREmployeeNumber_returnsCertificateProcessedByTestHREmployeeNumber() {
        //setup
        mockkObject(Scanner)
        val testScanData = Random.nextBytes(100)
        every { Scanner.getScanData() } returns testScanData
        val employeeNumber = Random.nextLong()
        for (certificateType in CertificateType.values()){
            val certificateRequest = CertificateRequest( employeeNumber = Random.nextLong() , certificateType = certificateType)
            val certificate = certificateRequest.process(TEST_HR_EMPLOYEE_NUMBER)

            //assert
            assertEquals(certificate.data, testScanData)
            assertEquals(certificate.processedBy, TEST_HR_EMPLOYEE_NUMBER)
            assertEquals(certificate.certificateRequest, certificateRequest)
            println("Passed with type = $certificateType")
        }
    }

 */
}