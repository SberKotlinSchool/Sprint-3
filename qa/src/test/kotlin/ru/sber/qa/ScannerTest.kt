package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class ScannerTest {

    val bytes = ByteArray(1)

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
        every { Random.nextBytes(any<Int>()) } returns bytes
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Random)
    }

    @Test
    fun getScanDataSuccess() {
        every { Random.nextLong(any(), any()) } returns 9999L

        val byteArray = assertDoesNotThrow { Scanner.getScanData() }
        assertEquals(bytes, byteArray)
    }

    @Test
    fun getScanDataScanTimeoutException() {
        every { Random.nextLong(any(), any()) } returns 10001L
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }
    }
}