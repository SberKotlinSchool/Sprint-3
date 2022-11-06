package ru.sber.oop

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RoomTest {

    private val roomSize = 20
    private val roomName = "bedroom"
    private val room = Room("bedroom", roomSize)
    private val townSquare = TownSquare()

    @Test
    fun testBase() {
        assertEquals(roomSize, room.size)
        assertEquals(roomName , room.name)
        assertEquals(1000, townSquare.size)
        assertEquals("Town Square", townSquare.name)
    }

}