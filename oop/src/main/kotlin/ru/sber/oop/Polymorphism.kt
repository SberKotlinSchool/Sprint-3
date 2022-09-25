package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(1,10)

    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed: Boolean
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) 2 * damageRoll else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        damageRoll.let {
            opponent.healthPoints -= it
            return it
        }
    }
}

class Goblin(
    override val powerType: String,
    override var healthPoints: Int,
    name: String,
    description: String
) : Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}