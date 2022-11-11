package ru.sber.oop

import java.util.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random().nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(
    val name: String,
    val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int,
    override val damageRoll: Int
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= damage

        return damage
    }
}

abstract class Monster : Fightable {
    abstract val name: String
    abstract val description: String

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll

        return damageRoll
    }
}

fun Monster.getSalutation()  = "I am a Goblin!!!"

class Goblin(
    override val powerType: String,
    override var healthPoints: Int,
    override val name: String,
    override val description: String
) : Monster() {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}


