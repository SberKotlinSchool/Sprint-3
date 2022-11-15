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

class Player(
    val name: String,
    val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(
    val name: String,
    val description: String,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(name: String, description: String, powerType: String, healthPoints: Int):
    Monster(name, description, powerType, healthPoints)
{
    override val damageRoll: Int
        get() = super.damageRoll/2
    }

fun Monster.getSalutation() = "Hello, I'm ${this.name}"


