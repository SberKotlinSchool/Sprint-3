package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(1,10)
    val name: String

    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    override val name : String,
    val isBlessed : Boolean
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll*2 else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

open class Monster(
    override val powerType: String,
    override var healthPoints: Int,
    override val name : String,
    val description : String
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(powerType: String, healthPoints: Int, name: String, description: String) : Monster(powerType, healthPoints, name, description) {

    override val damageRoll: Int
        get() = super.damageRoll/2
}


