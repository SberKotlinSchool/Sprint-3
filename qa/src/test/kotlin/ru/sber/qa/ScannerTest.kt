package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.random.Random
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class ScannerTest {

    @BeforeEach
    fun init() {
        mockkObject(Random)
    }

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @ParameterizedTest
    @ValueSource(longs = [1L, 5000L, 10000L])
    fun getScanDataSuccessful(nextLongParam: Long) {
        every { Random.nextLong(5000L, 15000L) } returns nextLongParam

        val result = Scanner.getScanData()

        assertNotNull(result)
        assertTrue { result.size == 100 }
    }

    @ParameterizedTest
    @ValueSource(longs = [10001L, 15000L])
    fun getScanDataThrow(nextLongParam: Long) {
        every { Random.nextLong(5000L, 15000L) } returns nextLongParam

        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

}