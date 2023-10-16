package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int get() = Random.nextInt(0, 10)

    fun attack(opponent: Fightable): Int
}

class Player(val name: String, val isBlessed: Boolean, override val powerType: String, override var healthPoints: Int) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val resultDamage = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= resultDamage
        if (opponent.healthPoints < 0)
            opponent.healthPoints = 0

        return resultDamage
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val resultDamage = damageRoll
        opponent.healthPoints -= resultDamage
        if (opponent.healthPoints < 0)
            opponent.healthPoints = 0

        return resultDamage
    }
}

fun Monster.getSalutation() = "Waaaagh!!!"

class Goblin(name: String, description: String, override val powerType: String, override var healthPoints: Int) : Monster(name, description) {
    override val damageRoll: Int get() = super.damageRoll / 2
}
