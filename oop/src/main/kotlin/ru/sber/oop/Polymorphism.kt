package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int get() = Random.nextInt(1, 11) // добавил свои ограничения

    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed: Boolean
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage: Int = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster : Fightable {
    abstract val name: String
    abstract val description: String

    override fun attack(opponent: Fightable): Int {
        val damage: Int = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(
    override val powerType: String,
    override var healthPoints: Int,
    override val name: String,
    override val description: String
) : Monster() {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}