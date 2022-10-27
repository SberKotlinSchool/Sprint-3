package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(val name: String, val isBlessed: Boolean = false) : Fightable {
    override val powerType = "hand"
    override var healthPoints = 100
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override val powerType = "hand"
    override var healthPoints = 100
    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(name: String, description: String) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}
