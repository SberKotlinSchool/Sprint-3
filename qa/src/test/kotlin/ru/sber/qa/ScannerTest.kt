package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertNotNull


internal class ScannerTest {


    @Test
    fun `getScanData() should throw ScanTimeoutException`() {
        // given
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 10_001L

        // then
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
        unmockkObject(Random)
    }


    @Test
    fun `getScanData() shouldn returnBytes`() {
        // given
        mockkObject(Random)
        every { Random.nextLong(any(), any()) } returns 1L

        // then
        assertNotNull(Scanner.getScanData())
        unmockkObject(Random)
    }



}