package ru.sber.qa

import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Scanner)
    }

    @Test
    fun getScanData() {
        assertEquals(Random.nextBytes(100).toString(), Scanner.getScanData().toString())
    }
}