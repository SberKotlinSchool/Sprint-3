import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import ru.sber.qa.ScanTimeoutException
import ru.sber.qa.Scanner
import kotlin.random.Random

private const val MIN_SCAN_DELAY = 5_000L
private const val MAX_SCAN_DELAY = 15_000L

private const val TEST_BYTE_ARRAY_SIZE = 100
private val TEST_BYTES = ByteArray(TEST_BYTE_ARRAY_SIZE)

class ScannerTests {

    @BeforeEach
    fun beforeTests() {
        mockkObject(Random)
        every { Random.nextBytes(TEST_BYTE_ARRAY_SIZE) } returns TEST_BYTES
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
    }

    @Test
    fun `test getScanData success`() {
        every { Random.nextLong(MIN_SCAN_DELAY, MAX_SCAN_DELAY) } returns MIN_SCAN_DELAY

        val scannedData: ByteArray = assertDoesNotThrow(Scanner::getScanData)
        assertArrayEquals(TEST_BYTES, scannedData)
    }

    @Test
    fun `test getScanData for ScanTimeoutException`() {
        every { Random.nextLong(MIN_SCAN_DELAY, MAX_SCAN_DELAY) } returns MAX_SCAN_DELAY

        assertThrows<ScanTimeoutException>(Scanner::getScanData)
    }
}
