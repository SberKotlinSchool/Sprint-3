package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(0, 50)

    fun attack(opponent: Fightable): Int
}

class Player (
    override val powerType: String,
    override var healthPoints: Int,
    override val damageRoll: Int,
    val name : String,
    var isBlessed : Boolean
    ) : Fightable {
    override fun attack(opponent: Fightable): Int {
        var damageThisRound = damageRoll
        if (isBlessed)
            damageThisRound *= 2
        opponent.healthPoints -= damageThisRound
        return damageThisRound
    }
}

abstract class Monster  (
    override val powerType: String,
    override var healthPoints: Int,
    open val name : String,
    open val description : String
    ) : Fightable {
    override val damageRoll
        get() = super.damageRoll
    override fun attack(opponent: Fightable): Int {
        var damageThisRound = damageRoll
        opponent.healthPoints -= damageThisRound
        return damageThisRound
    }
}

class Goblin (
    override val powerType: String,
    override var healthPoints: Int,
    override val name : String,
    override val description : String
        ) : Monster(powerType, healthPoints, name, description) {

    override val damageRoll
        get() = super.damageRoll / 2

}



