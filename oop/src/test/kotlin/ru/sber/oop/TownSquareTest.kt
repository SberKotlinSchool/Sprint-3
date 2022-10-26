package ru.sber.oop

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TownSquareTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `create Town Square with name Town Square and size 1000`() {
        val townSquare = TownSquare()
        assertEquals("Town Square", townSquare.name)
        assertEquals(1000, townSquare.size)
    }

    @Test
    fun `load and parent load not equals`() {
        val room = Room("Test", 1)
        val townSquare = TownSquare()
        assertNotEquals(room.load(), townSquare.load())
    }

    @Test
    fun `second constructor with default size 100`() {
        val room = Room("Test")
        assertEquals(100, room.size)
    }
}