package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(0, 100)


    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...

class Player(
    private val name: String,
    private val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll

        return if (opponent.healthPoints > 0) {
            opponent.healthPoints -= damage
            damage
        } else 0
    }

    override val damageRoll: Int
        get() = Random.nextInt(0, 100)
}

abstract class Monster(
    private val name: String,
    private val description: String
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        return if (opponent.healthPoints > 0) {
            opponent.healthPoints -= damageRoll
            damageRoll
        } else 0
    }
}

class Goblin(
    private val name: String,
    private val description: String,
    override val powerType: String,
    override var healthPoints: Int
) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation() = "I’ll kill you in a jiffy"

