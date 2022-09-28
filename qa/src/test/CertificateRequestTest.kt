import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.sber.qa.CertificateRequest
import ru.sber.qa.CertificateType
import ru.sber.qa.Scanner
import kotlin.random.Random

internal class CertificateRequestTest {
    private val hrEmployeeNumber = 99999L
    private val certRequestLabourBook = CertificateRequest(
        hrEmployeeNumber,
        certificateType = CertificateType.LABOUR_BOOK
    )
    private val certRequestNDFL = CertificateRequest(
        hrEmployeeNumber,
        certificateType = CertificateType.NDFL
    )

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)

    }
    @Test
    fun processForLabourBook() {
        val data = Random.nextBytes(100)
        every { Scanner.getScanData() } returns data

        val certificate = certRequestLabourBook.process(hrEmployeeNumber)

        assertNotNull(certificate)
        assertEquals(certificate.certificateRequest, certRequestLabourBook)
        assertEquals(certificate.processedBy, hrEmployeeNumber)
        assertEquals(certificate.data, data)

        verify(exactly = 1) { Scanner.getScanData() }
    }


    @Test
    fun processForNDFL() {
        val data = Random.nextBytes(100)
        every { Scanner.getScanData() } returns data

        val certificate = certRequestNDFL.process(hrEmployeeNumber)

        assertNotNull(certificate)
        assertEquals(certificate.certificateRequest, certRequestNDFL)
        assertEquals(certificate.processedBy, hrEmployeeNumber)
        assertEquals(certificate.data, data)

        verify(exactly = 1) { Scanner.getScanData() }
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

}