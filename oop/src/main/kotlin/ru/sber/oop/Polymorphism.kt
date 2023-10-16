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
    override val powerType: String,
    override var healthPoints: Int,
    var isBlessed: Boolean = false
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        var damage = damageRoll
        if (isBlessed) damage *= 2
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster : Fightable {
    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin : Monster() {
    override fun attack(opponent: Fightable): Int {
        return super.attack(opponent)
    }

    override val powerType: String
        get() = "wind"
    override var healthPoints: Int = 100

    override val damageRoll: Int
        get() = super.damageRoll / 2
}


