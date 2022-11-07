package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() {
            return Random.nextInt()
        }

    fun attack(opponent: Fightable): Int
}

class Player(val name: String, val isBlessed: Boolean, override val powerType: String, override var healthPoints: Int) :
    Fightable {
    override fun attack(opponent: Fightable) = if (isBlessed) {
        2 * damageRoll
    } else {
        damageRoll
    }.also {
        opponent.healthPoints -= it
    }

}

abstract class Monster(val name: String, val description: String) : Fightable {

    override fun attack(opponent: Fightable) = damageRoll.also {
        opponent.healthPoints -= it
    }

}

class Goblin(name: String, description: String, override val powerType: String, override var healthPoints: Int) :
    Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2

}