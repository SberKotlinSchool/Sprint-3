package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CertificateRequestTest {

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @ParameterizedTest
    @MethodSource("getData")
    fun testProcessSuccessful(certificateType: CertificateType, scanData: ByteArray, hrEmployeeNumber: Long) {
        val certificateRequest = CertificateRequest(hrEmployeeNumber, certificateType)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns scanData

        val result = certificateRequest.process(hrEmployeeNumber)

        assertNotNull(result)
        assertAll("Expected result are right",
            Executable { assertEquals(scanData, result.data) },
            Executable { assertEquals(hrEmployeeNumber, result.processedBy) },
            Executable { assertEquals(certificateRequest, result.certificateRequest) }
        )
    }

    private fun getData() = Stream.of(
        Arguments.of(CertificateType.LABOUR_BOOK, "string".toByteArray(), 1L),
        Arguments.of(CertificateType.NDFL, "test".toByteArray(), 10L)
    )

}