package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

internal class ScannerTest {

    val random = Random.nextBytes(1)

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns random
    }

    @Test
    fun getScanData() {
        assertEquals(random, Scanner.getScanData())
    }
}