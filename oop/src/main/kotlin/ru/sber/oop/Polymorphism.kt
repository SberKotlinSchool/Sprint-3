package ru.sber.oop

import java.util.*


interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random().nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(val name: String, var isBlessed: Boolean = false): Fightable {
    override val powerType: String = "super power"
    override var healthPoints: Int = 1000

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(val name: String, val description: String): Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(name: String, description: String): Monster(name, description) {
    override val powerType: String = "low power"
    override var healthPoints: Int = 10

    override val damageRoll: Int
        get() = super.damageRoll / 2

}
