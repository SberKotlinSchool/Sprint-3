import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.sber.qa.CertificateRequest
import ru.sber.qa.CertificateType
import ru.sber.qa.Scanner

internal class CertificateRequestTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Scanner)
    }

    @Test
    fun `test process correct output`() {
        //given
        val hrEmployeeNumber = 0L
        val certificateRequest = CertificateRequest(hrEmployeeNumber, CertificateType.LABOUR_BOOK)
        val expectedByteArray = ByteArray(1)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns expectedByteArray

        //when
        val actualCertificate = certificateRequest.process(hrEmployeeNumber)

        //then
        verify(exactly = 1) { Scanner.getScanData() }

        assertEquals(expectedByteArray, actualCertificate.data)
        assertEquals(hrEmployeeNumber, actualCertificate.processedBy)
        assertEquals(certificateRequest, actualCertificate.certificateRequest)
    }
}