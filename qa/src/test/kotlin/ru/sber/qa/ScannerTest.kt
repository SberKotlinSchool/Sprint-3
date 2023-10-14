package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getScanData when scan duration is less than threshold`() {
        // Мокирование Random.nextLong и Random.nextBytes
        every { Random.nextLong(5000L, 15000L) } returns 7000L
        every { Random.nextBytes(100) } returns byteArrayOf(1, 2, 3)

        val scanData = Scanner.getScanData()

        // Проверка, что возвращенные данные совпадают с ожидаемыми
        assertArrayEquals(byteArrayOf(1, 2, 3), scanData)
    }

    @Test
    fun `test getScanData when scan duration is equal to threshold`() {
        // Мокирование Random.nextLong и Random.nextBytes
        every { Random.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD
        every { Random.nextBytes(100) } returns byteArrayOf(4, 5, 6)
        val scanData = Scanner.getScanData()
        // Проверка, что возвращенные данные совпадают с ожидаемыми
        assertArrayEquals(byteArrayOf(4, 5, 6), scanData)
    }

    @Test
    fun `test getScanData when scan duration is greater than threshold`() {
        // Мокирование Random.nextLong для возвращения значения больше SCAN_TIMEOUT_THRESHOLD
        every { Random.nextLong(5000L, 15000L) } returns (Scanner.SCAN_TIMEOUT_THRESHOLD + 1000)

        assertThrows(ScanTimeoutException::class.java) {
            Scanner.getScanData()
        }
    }
}