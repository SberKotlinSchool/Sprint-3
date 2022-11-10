package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
    get() = Random.nextInt(1000)

    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed: Boolean = false) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {
            damageRoll * 2
        } else damageRoll

        opponent.healthPoints -= damage
        return damage
    }

}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
    val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(name: String, description: String, override val powerType: String, override var healthPoints: Int) :
    Monster(name, description) {
    override val damageRoll: Int
    get() = super.damageRoll / 2
}

fun Monster.getSalutation(): String = "Hi"





