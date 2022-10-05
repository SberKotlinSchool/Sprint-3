package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import kotlin.random.Random

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
    fun process() {
        // given
        val expected = "mockk".repeat(100/5).toByteArray()
        every { Scanner.getScanData() } returns expected
        val hrNumber = 2L
        val employeeNumber = 1L

        // when
        val certReq = CertificateRequest(employeeNumber, CertificateType.LABOUR_BOOK)
        val cert = certReq.process(hrNumber)

        // then
        assertEquals(cert.data, expected)
        assertEquals(cert.processedBy, hrNumber)
    }
}