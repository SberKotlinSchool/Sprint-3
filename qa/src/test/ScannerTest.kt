import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.sber.qa.ScanTimeoutException
import ru.sber.qa.Scanner
import kotlin.random.Random


internal class ScannerTest {
    val scanner = Scanner
    val mockedRandom = mockkObject(Random)

    @Test
    fun testGetScanData() {

        every { Random.nextLong(5000L, 15000L) } returns 9999L

        val scanData = scanner.getScanData()

        assertInstanceOf(ByteArray::class.java, scanData)
        assertTrue(scanData.size == 100)
    }

    @Test
    fun testThrowScanTimeoutException() {
        every { Random.nextLong(5000L, 15000L) } returns 10001L

        assertThrows(ScanTimeoutException::class.java) {
            scanner.getScanData()
        }
    }
}