package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int

    val damageRoll: Int
        get() = Random.Default.nextInt(Int.MIN_VALUE / 2, Int.MAX_VALUE / 2)


    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed: Boolean = false
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        var damage = damageRoll

        if (isBlessed) damage *= 2

        opponent.healthPoints -= damage
        return damage
    }

}

abstract class Monster(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val description: String
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll

        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(powerType: String, healthPoints: Int, name: String, description: String) :
    Monster(powerType, healthPoints, name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2

}

fun main() {
    val a = Player("asd", 100, "John")
    println(a.damageRoll)
}

//TODO: create class Player, Monster, Goblin here...


