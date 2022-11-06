package ru.sber.oop

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MonsterTest{

    private val monsterHealthPoints = 50
    private val monsterPowerType = "dirt"
    private val goblin = Goblin(monsterPowerType, monsterHealthPoints)
    private val healthPoints = 30
    private val powerType = "dirt"
    private val player = Player(powerType = powerType, healthPoints = healthPoints, isBlessed = true)

    @Test
    fun testBase() {
        val goblinHealthPoints = goblin.healthPoints
        val playerDamageRoll = player.attack(goblin)
        assertEquals(goblinHealthPoints - playerDamageRoll, goblin.healthPoints)
    }
}