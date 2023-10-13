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
    val name: String,
    val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll * (if (isBlessed) 2 else 1)
        opponent.healthPoints -= damage
        return damage
    }

}

abstract class Monster(
    open val name: String,
    open val description: String,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(
    override val name: String,
    override val description: String,
    override val powerType: String,
    override var healthPoints: Int
) : Monster(name, description, powerType, healthPoints) {
    override val damageRoll: Int
        get() = Random.nextInt() / 2
}

fun Monster.getSalutation(): String = "I'm the big monster $name!"


