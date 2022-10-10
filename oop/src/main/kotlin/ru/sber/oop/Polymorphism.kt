package ru.sber.oop

import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() { return Random.nextInt(0, 100) }

    fun attack(opponent: Fightable): Int

}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    var name: String,
    var isBlessed: Boolean
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        var damage = if (isBlessed) damageRoll * 2 else damageRoll
        damage = min(damage, opponent.healthPoints)
        opponent.healthPoints -= damage

        return damage
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = min(damageRoll, opponent.healthPoints)
        opponent.healthPoints -= damage

        return damage
    }
}

class Goblin(
    name: String,
    description: String,
    override val powerType: String,
    override var healthPoints: Int
) : Monster(name, description) {

    override val damageRoll: Int
        get() = sqrt(super.damageRoll.toDouble()).toInt()
}

fun Monster.getSalutation(): String = "Take it!"