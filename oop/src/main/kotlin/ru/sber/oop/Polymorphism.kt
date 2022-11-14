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
    override var healthPoints: Int,
    var isBlessed: Boolean = false
) : Fightable {

    override val powerType: String = "Intelligent, adopts experience of the opponent"

    override fun attack(opponent: Fightable): Int {
        val finalDamage = if (isBlessed) damageRoll * 2 else damageRoll

        return finalDamage.also { opponent.healthPoints -= it }
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {

    override fun attack(opponent: Fightable): Int =
        damageRoll.also { opponent.healthPoints -= it }
}

class Goblin(
    override var healthPoints: Int = 10
) : Monster("Goblin", "A goblin is a mythical creature of Germanic and British folklore.") {

    override val powerType: String = "Steal horses, hide small objects"

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation(): String =
    "Hey, I'm $name!"
