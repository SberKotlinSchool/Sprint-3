package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import ru.sber.qa.Scanner
import kotlin.test.assertEquals

internal class ScannerTest {

    @Test
    fun scannerTest_randomNextLongMoreThanMAXCONST_throwTimeoutException(){
        //setup
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns (Scanner.SCAN_TIMEOUT_THRESHOLD + 1)
        //Возможно, тут не стоит писать константы в виде 5000L, но в исходном классе так, поэтому и в тесте так

        //assert
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun scannerTest_randomNextLongLessThanMAXCONST_returnsByteArray(){
        //setup
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns Random.nextLong (5000L , Scanner.SCAN_TIMEOUT_THRESHOLD)
        val byteArray = Random.nextBytes(100)
        every { Random.nextBytes(100) } returns byteArray

        //assert
        assertEquals(byteArray , Scanner.getScanData())
    }
}