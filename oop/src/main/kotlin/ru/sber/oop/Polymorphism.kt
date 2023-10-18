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

class Player(override val powerType: String, override var healthPoints: Int, val name: String, val isBlessed: Boolean) :
    Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2
        else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster : Fightable {
    abstract val name: String
    abstract val description: String

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(
    override val name: String,
    override val description: String,
    override val powerType: String,
    override var healthPoints: Int,
) : Monster() {
    override val damageRoll = super.damageRoll / 2
}

fun Monster.getSalutation(): String = "Hello World!"

