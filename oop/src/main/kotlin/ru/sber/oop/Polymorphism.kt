package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(0, 7000)

    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...

class Goblin(
    override val name: String,
    override val description: String = "Weak monster",
    override val powerType: String,
    override var healthPoints: Int
) : Monster(), Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll / 2
        println("Attacking opponent for $damage")
        opponent.healthPoints -= damage
        return damage
    }

}

abstract class Monster() : Fightable {
    abstract val name: String
    abstract val description: String

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        println("Attacking opponent for $damage")
        opponent.healthPoints -= damage
        return damageRoll
    }

}

class Player(
    val name: String,
    private val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        var damage = damageRoll

        if (isBlessed) {
            damage *= 2
        }

        println("$name attacking opponent for $damage")
        opponent.healthPoints -= damage
        return damage

    }
}

fun main() {
    val player1 = Player("A1", true, "magic", 10000)
    val player2 = Player("A2", false, "physic", 20000)

    println(player2.healthPoints)
    player1.attack(player2)
    println(player2.healthPoints)
}

