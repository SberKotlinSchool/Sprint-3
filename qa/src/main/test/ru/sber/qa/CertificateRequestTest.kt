package ru.sber.qa


import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

@SuppressWarnings("unchecked", "deprecated")
internal class CertificateRequestTest {

    @SpyK
    val ct1 : CertificateType = CertificateType.NDFL
    @SpyK
    val ct2 : CertificateType = CertificateType.LABOUR_BOOK
    @MockK
    lateinit var cr1 : CertificateRequest
    @MockK
    lateinit var cr2 : CertificateRequest

    @BeforeEach
    internal fun setUp() {
        cr1 = mockk(relaxed = true)
        cr2 = mockk(relaxed = true)
    }

    @Test
    fun process() {

        every { cr1.process(1) } returns
                Certificate(cr1, 1, Random.nextBytes(100))

        assertEquals(Certificate(cr1,
            1,
            Random.nextBytes(100)).certificateRequest.employeeNumber,
            cr1.process(1).certificateRequest.employeeNumber)
    }

    @Test
    fun getEmployeeNumber() {
        every { cr1.employeeNumber } returns 1
        every { cr2.employeeNumber } returns 2
        assertEquals(1, cr1.employeeNumber)
        assertEquals(2, cr2.employeeNumber)
    }

    @Test
    fun getCertificateType() {
        assertEquals(CertificateType.NDFL, ct1)
        assertEquals(CertificateType.LABOUR_BOOK, ct2)
    }
}