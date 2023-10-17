package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(override val powerType: String,
             override var healthPoints: Int,
             val name: String,
             val isBlessed: Boolean) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val shot = damageRoll
        val damage = if (isBlessed) shot else shot * 2
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(val name: String,
                       val description: String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(override val powerType: String,
             override var healthPoints: Int) : Monster("Гоблин", "Вонючий хмырь") {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}


