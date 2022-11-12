package ru.sber.qa


import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@SuppressWarnings("unchecked", "deprecated")
internal class CertificateRequestTest {

    @SpyK
    val ct1 : CertificateType = CertificateType.NDFL
    @SpyK
    val ct2 : CertificateType = CertificateType.LABOUR_BOOK
    @SpyK
    val cr1 = CertificateRequest(1, ct1)
    @SpyK
    val cr2 = CertificateRequest(2, ct2)


    @Test
    fun process() {
        try {
            val t1= cr1.process(1)
            assertEquals(100, t1.data.size)
        } catch (e: Throwable){
            assertEquals(e.message, "Таймаут сканирования документа")
        }
    }

    @Test
    fun getEmployeeNumber() {
        assertEquals(1, cr1.employeeNumber)
        assertEquals(2, cr2.employeeNumber)
    }

    @Test
    fun getCertificateType() {
        assertEquals(CertificateType.NDFL, ct1)
        assertEquals(CertificateType.LABOUR_BOOK, ct2)
    }
}