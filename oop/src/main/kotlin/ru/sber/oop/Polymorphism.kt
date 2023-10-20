package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(0, 5)

    fun attack(opponent: Fightable): Int
}

class Player() : Fightable {
    override var healthPoints: Int = 10
    override val powerType: String = "Magic"
    val name: String = "Bob"
    var isBlessed: Boolean = false

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= damage
        return damage
    }
}

open class Monster : Fightable {
    override var healthPoints: Int = 12
    override val powerType: String = "Power"
    val name: String = "Tom"
    val description: String = "Terrible $name"

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin : Monster() {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

