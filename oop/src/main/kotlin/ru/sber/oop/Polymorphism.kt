package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(100)

    fun attack(opponent: Fightable): Int
}

class Player(
    val name: String,
    val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int

) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= damage;
        return damage
    }
}


abstract class Monster(
    open val name: String,
    open val description: String,
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(
    override val name: String,
    override val description: String,
    override val powerType: String,
    override var healthPoints: Int
) : Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation() = "Hi! I brought THREE HUNDRED BUCKS!"