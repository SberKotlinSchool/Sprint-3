package ru.sber.oop

import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RoomTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun closeUp() {
        unmockkAll()
    }

    @Test
    fun shouldLoadRoom() {
        val room: Room = Room("CastleBigRoom")
        assertEquals(LOAD_ROOM_RESULT, room.load())
    }

    @Test
    fun shouldReturnName() {
        val room: Room = Room("CastleBigRoom")
        assertNotNull(room.description())
    }
}