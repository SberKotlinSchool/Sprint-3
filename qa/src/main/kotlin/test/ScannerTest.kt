package test

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.sber.qa.*
import java.time.Duration.*
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
        mockkObject(Scanner)
    }

    @ParameterizedTest
    @MethodSource("GetScanDataThrowScanTimeoutException_ThreadSleepConstParams")
    fun testGetScanDataThrowScanTimeoutException(scanDuration: Long){
        //given
        every { Random.nextLong(5000L, 15000L) } returns scanDuration
        //result
        assertThrows(ScanTimeoutException::class.java, {Scanner.getScanData()},
            "Не выполнено со scanDuration: $scanDuration"
        )
    }

    @ParameterizedTest
    @MethodSource("GetScanDataDoesNotThrow_ThreadSleepRandomParams")
    fun testGetScanDataReturnRandomByteArray_100(scanDuration: Long){
        //given
        every { Random.nextLong(5000L, 15000L) } returns scanDuration
        //result
        assertDoesNotThrow ({ Scanner.getScanData()},
            "Не выполнено со scanDuration: $scanDuration"
        )
    }

    @ParameterizedTest
    @MethodSource("GetScanDataThrowScanTimeoutException_ThreadSleepConstParams")
    fun testGetScanDataThreadSleepConst(scanDuration: Long){
        //given
        val permissibleVariation = 500L
        every { Random.nextLong(5000L, 15000L) } returns scanDuration
        every { Scanner.getScanData() } returns Random.nextBytes(100)
        //result
        assertTimeoutPreemptively(ofMillis(10000L + permissibleVariation), {Scanner.getScanData()},
            "Не выполнено со scanDuration: $scanDuration"
        )
    }

    @ParameterizedTest
    @MethodSource("GetScanDataDoesNotThrow_ThreadSleepRandomParams")
    fun testGetScanDataThreadSleepRandom(scanDuration: Long){
        //given
        val permissibleVariation = 500L
        every { Random.nextLong(5000L, 15000L) } returns scanDuration
        //result
        assertTimeoutPreemptively(ofMillis(scanDuration + permissibleVariation), {Scanner.getScanData()},
            "Не выполнено со scanDuration: $scanDuration"
        )
    }

    companion object{
        @JvmStatic
        fun GetScanDataThrowScanTimeoutException_ThreadSleepConstParams() = listOf(
            Arguments.of(Random.nextLong(10001L, 15000L)),
            Arguments.of(10001L),
            Arguments.of(15000L)
        )

        @JvmStatic
        fun GetScanDataDoesNotThrow_ThreadSleepRandomParams() = listOf(
            Arguments.of(Random.nextLong(5000L, 10000L)),
            Arguments.of(5000L),
            Arguments.of(10000L)
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}