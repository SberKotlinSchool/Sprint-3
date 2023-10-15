package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...
class Player(
    override var healthPoints: Int,
    override val powerType: String,
    val name: String,
    private val isBlessed: Boolean
) :
    Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll

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
    val name: String,
    val description: String,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(name: String, description: String, powerType: String, healthPoints: Int) : Monster(
    name, description, powerType, healthPoints
) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation(): String {
    return "This is ${this.name}"
}