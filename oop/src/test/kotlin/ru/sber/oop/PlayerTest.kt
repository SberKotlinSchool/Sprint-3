package ru.sber.oop

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PlayerTest {

    val blessedPlayer: Player = Player("Terran", 50, "Gladiator", true)
    val unblessedPlayer: Player = Player("Terran", 40, "Gladiator", false)
    var goblin: Goblin = Goblin("Goblin", 80, "Gobly", "LittleGoblin")

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
        every { Random.nextInt() } returns 20
    }

    @AfterEach
    fun closeUp() {
        unmockkAll()
    }

    @Test
    fun shouldBlessedPlayerMakeBigDamageToGoblin() {
        assertEquals(40, blessedPlayer.attack(goblin))
    }

    @Test
    fun shouldUnblessedPlayerMakeLessDamageToGoblin() {
        assertEquals(20, unblessedPlayer.attack(goblin))
    }

    @Test
    fun shouldDamagePlayer() {
        assertEquals(10, goblin.attack(blessedPlayer))
    }
}