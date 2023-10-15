package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class)
class ScannerTest {
    companion object {
        private const val MORE_THAN_TIMEOUT_RANDOM_VALUE = Scanner.SCAN_TIMEOUT_THRESHOLD + 1
        private const val LESS_THAN_TIMEOUT_RANDOM_VALUE = Scanner.SCAN_TIMEOUT_THRESHOLD - 1

        @JvmStatic
        @AfterAll
        fun afterAll() {
            unmockkAll()
        }
    }

    @BeforeEach
    fun beforeEach() {
        mockkObject(Random)
    }

    @Test
    fun testScanDurationMoreThanTimeout() {
        every { Random.nextLong(any(), any()) } returns MORE_THAN_TIMEOUT_RANDOM_VALUE
        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }
    }

    @Test
    fun testScanDurationLessThanTimeout() {
        every { Random.nextLong(any(), any()) } returns LESS_THAN_TIMEOUT_RANDOM_VALUE
        Scanner.getScanData()
        verify { Random.nextBytes(100) }
    }
}