package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

internal class CertificateRequestTest {

    @BeforeEach
    fun setup() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun process() {
        // Arrange
        val hrEmployeeNumber = Random.nextLong()
        val employeeNumber = Random.nextLong()
        val certificateType = CertificateType.values().random()
        val certificateRequest = CertificateRequest(employeeNumber, certificateType)
        val expectedByteArray = "SimpleText".toByteArray()

        every {
            Scanner.getScanData()
        } returns expectedByteArray

        // Act
        val result = certificateRequest.process(hrEmployeeNumber)

        // Assert
        assertEquals(result.data, expectedByteArray)
        assertEquals(result.processedBy, hrEmployeeNumber)
        assertEquals(result.certificateRequest, certificateRequest)
    }
}