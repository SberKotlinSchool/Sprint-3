package ru.sber.oop

import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TownSquareTest {
    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun closeUp() {
        unmockkAll()
    }

    @Test
    fun shouldLoadTownSquare() {
        val townSquare: TownSquare = TownSquare()
        assertNotNull(townSquare.load())
    }
}