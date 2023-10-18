package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(override val powerType: String, override var healthPoints: Int, val name: String, val isBlessed: Boolean) :
    Fightable {
    override fun attack(opponent: Fightable): Int {
        var fullDamageRoll: Int = damageRoll

        if (isBlessed) fullDamageRoll += damageRoll

        opponent.healthPoints - fullDamageRoll
        return fullDamageRoll
    }

}

abstract class Monster(open val name: String, open val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        var fullDamageRoll: Int = damageRoll

        opponent.healthPoints - fullDamageRoll
        return fullDamageRoll
    }
}

fun Monster.getSalutation() = "A salute from monster!"

class Goblin(
    override val powerType: String,
    override var healthPoints: Int,
    override val name: String,
    override val description: String
) :
    Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2

}
