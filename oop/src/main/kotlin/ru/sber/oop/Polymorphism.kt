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
        if (!isBlessed) {
            healthPoints -= damageRoll
            return damageRoll
        }
        healthPoints -= damageRoll * 2
        return damageRoll * 2
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(override val powerType: String, override var healthPoints: Int, name: String, description: String) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}


