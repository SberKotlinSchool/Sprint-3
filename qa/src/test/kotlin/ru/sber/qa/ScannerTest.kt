package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.random.Random
import kotlin.test.assertFailsWith

internal class ScannerTest {

    @BeforeEach
    fun mockkRandom() {
        mockkObject(Random)
    }

    @AfterEach
    fun deleteMockk() {
        unmockkAll()
    }
    @org.junit.jupiter.api.Test
    fun getScanDataTest() {

        every { Random.nextLong(5000L, 15000L) } returnsMany listOf( 14999L , 5000L)

        assertFailsWith<ScanTimeoutException> { Scanner.getScanData() }

        assertDoesNotThrow { Scanner.getScanData() }

    }
}