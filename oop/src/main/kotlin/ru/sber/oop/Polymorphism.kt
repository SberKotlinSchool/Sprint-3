package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(override val powerType: String, override var healthPoints: Int, val name: String, val isBlessed: Boolean) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {
            damageRoll
        } else {
            damageRoll * 2
        }
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(open val name: String, open val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(override val powerType: String, override var healthPoints: Int, override val name: String, override val description: String) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}


