package ru.sber.oop

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class PlayerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
        every { Random.nextInt() } returns 10
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `player damageRoll random`() {
        val player = Player("Test", false)
        val damage = player.damageRoll
        verify { Random.nextInt() }
        assertEquals(10, damage)
    }

    @Test
    fun `player damageRoll random blessed`() {
        val player = Player("Test", true)
        val player2 = Player("Test2", false)
        val damage = player.attack(player2)
        verify { Random.nextInt() }
        assertEquals(20, damage)
    }

    @Test
    fun `monster damageRoll random`() {
        val monster = Goblin("Goblin1", "true")
        val player2 = Player("Test2", false)
        val damage = monster.attack(player2)
        verify { Random.nextInt() }
        assertEquals(5, damage)
    }

    @Test
    fun `monster getSalutation check`() {
        val room = Room("Goblin Room")
        val sound = room.load()
        assertEquals("Nothing much to see here...goblin", sound)
    }
}
