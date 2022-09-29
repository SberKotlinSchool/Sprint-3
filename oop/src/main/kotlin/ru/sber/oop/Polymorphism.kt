package ru.sber.oop

import kotlin.math.min
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
    private val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll * if (isBlessed) 2 else 1

        opponent.healthPoints - damage

        // Мы же не можем нанести больше урона, чем есть здоровья у врага
        return min(opponent.healthPoints, damage)
    }
}

abstract class Monster(
    val name: String,
    val description: String
): Fightable {

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints - damageRoll
        return min(opponent.healthPoints, damageRoll)
    }
}

class Goblin(
    override val powerType: String,
    override var healthPoints: Int,
): Monster("Goblin", "little green brat") {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

val Room.enemy: Goblin
    get() = Goblin(
        powerType = "Nasty scream",
        healthPoints = Random.nextInt(10, 20)
    )

fun Monster.getSalutation() = "$name is waving to you"
