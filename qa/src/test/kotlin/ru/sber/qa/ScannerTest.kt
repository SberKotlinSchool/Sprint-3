package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.sber.qa.Scanner.SCAN_TIMEOUT_THRESHOLD
import kotlin.Long.Companion.MAX_VALUE
import kotlin.random.Random

internal class ScannerTest {

    @ParameterizedTest(name = "givenScannerWith{0}ScanDuration_whenGetScanData_thenByteArray")
    @ValueSource(longs = [0, SCAN_TIMEOUT_THRESHOLD - 1, SCAN_TIMEOUT_THRESHOLD / 2])
    fun getScanDataSuccessScanTest(duration: Long) {
        mockkObject(Random)
        every {
            Random.nextLong(5000L, 15000L)
        } returns duration
        val byteArray = ByteArray(100)
        every {
            Random.nextBytes(100)
        } returns byteArray
        assertEquals(byteArray, Scanner.getScanData())
        unmockkAll()
    }

    @ParameterizedTest(name = "givenScannerWith{0}ScanDuration_whenGetScanData_thenScanTimeoutException")
    @ValueSource(longs = [SCAN_TIMEOUT_THRESHOLD + 1, MAX_VALUE, (MAX_VALUE - SCAN_TIMEOUT_THRESHOLD) / 2])
    fun getScanDataTimeoutScanTest(duration: Long) {
        mockkObject(Random)
        every {
            Random.nextLong(5000L, 15000L)
        } returns duration

        assertThrows(ScanTimeoutException::class.java) {
            Scanner.getScanData()
        }
        unmockkAll()
    }


}