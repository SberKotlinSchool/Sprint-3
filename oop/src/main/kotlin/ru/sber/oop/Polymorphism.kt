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

