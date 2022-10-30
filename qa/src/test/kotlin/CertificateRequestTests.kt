import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import ru.sber.qa.CertificateRequest
import ru.sber.qa.CertificateType
import ru.sber.qa.Scanner
import kotlin.test.assertEquals

private const val CERTIFICATE_REQUEST_NUM = 2
private const val TEST_HR_EMPLOYEE_NUMBER = 666L

private const val TEST_BYTE_ARRAY_SIZE = 100
private val TEST_BYTE_ARRAY = ByteArray(TEST_BYTE_ARRAY_SIZE)

class CertificateRequestTests {

    private val certificateRequests = Array(CERTIFICATE_REQUEST_NUM) { index ->
        CertificateRequest(index.toLong(), CertificateType.values()[index])
    }

    @BeforeEach
    fun beforeEachTest() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns TEST_BYTE_ARRAY
    }

    @AfterEach
    fun afterEachTest() {
        unmockkAll()
    }

    /**
     * Обходит все инстансы CertificateRequest и проверяет каждое поле после вызова process
     * Выполняет все проверки и только после этого выводит сообщение об ошибке
     */
    @Test
    fun `test CertificateRequest is properly generated`() {
        certificateRequests
            .map { request ->
                {
                    with(request.process(TEST_HR_EMPLOYEE_NUMBER)) {
                        assertAll("check ${request.certificateType.name} request processing",
                            { assertEquals(request.employeeNumber, certificateRequest.employeeNumber) },
                            { assertEquals(request.certificateType, certificateRequest.certificateType) },
                            { assertArrayEquals(TEST_BYTE_ARRAY, data) },
                            { assertEquals(TEST_HR_EMPLOYEE_NUMBER, processedBy) })
                    }
                }
            }
            .let { checks ->
                assertAll(checks)
                verify(exactly = CERTIFICATE_REQUEST_NUM) { Scanner.getScanData() }
            }
    }
}
