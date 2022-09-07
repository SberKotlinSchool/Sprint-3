package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class ScannerTest {

    private val mockScanner = spyk<Scanner>()

    @Test
    fun getScanData() {
        mockkStatic(Random::class) {

            every { Random.nextLong() } returns 1

            val scanData = mockScanner.getScanData()

            assertNotNull(scanData)
        }


    }
}