package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.*
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun beforeEach() = mockkObject(Random)

    @Test
    fun `getScanData() success test`() {
        val array = ByteArray(1)

        every { Random.nextLong(5_000L, 15_000L) } returns 1L
        every { Random.nextBytes(100) } returns array

        Assertions.assertEquals(array, Scanner.getScanData())
    }

    @Test
    fun `getScanData() throws ScanTimeoutException`() {
        every { Random.nextLong(5_000L, 15_000L) } returns 15_001L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }

    @AfterEach
    fun afterEach() = unmockkObject(Random)
}