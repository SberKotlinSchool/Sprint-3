package ru.sber.qa.test

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import ru.sber.qa.CertificateRequest
import ru.sber.qa.CertificateType
import ru.sber.qa.Scanner
import kotlin.random.Random

internal class CertificateRequestTest {

    @Test
    fun process() {
        // given
        mockkObject(Scanner)
        val dataMock = Random.Default.nextBytes(10)
        every { Scanner.getScanData() } returns dataMock
        val certificateRequest = CertificateRequest(
                employeeNumber = 100L,
                certificateType = CertificateType.NDFL
        )

        // when
        val result = certificateRequest.process(hrEmployeeNumber = 200L)

        // then
        assertEquals(result.certificateRequest, certificateRequest)
        assertEquals(result.processedBy, 200L)
        assertEquals(result.data, dataMock)

        verify(exactly = 1) { Scanner.getScanData() }

        unmockkAll()
    }
}