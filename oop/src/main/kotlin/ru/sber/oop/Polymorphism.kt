package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed: Boolean
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damageValue = if (isBlessed) damageRoll else damageRoll * 2
        opponent.healthPoints -= damageValue
        return damageValue
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damageValue = damageRoll
        opponent.healthPoints -= damageValue
        return damageValue
    }
}

class Goblin(name: String, description: String, override val powerType: String, override var healthPoints: Int) :
    Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}


