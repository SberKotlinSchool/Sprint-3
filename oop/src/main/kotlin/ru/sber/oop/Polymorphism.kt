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
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    private val isBlessed: Boolean
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = this.damageRoll

        return if (this.isBlessed) {
            opponent.healthPoints -= damage * 2
            damage * 2
        } else {
            opponent.healthPoints -= damage
            damage
        }
    }
}

open class Monster(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val description: String
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = this.damageRoll

        opponent.healthPoints -= damage

        return damage
    }
}

class Goblin(powerType: String,
             healthPoints: Int,
             name: String,
             description: String
) : Monster(powerType, healthPoints, name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation(): String {
    return "I'm ${this.name}"
}
