package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(from = 1, until = 1000)

    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...
class Player(val name: String, val isBlessed: Boolean) : Fightable {
    override val powerType: String
        get() = TODO("Not yet implemented")
    override var healthPoints: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(
    name: String,
    description: String,
    override val powerType: String,
    override var healthPoints: Int,
) : Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation(): String {
    return "Hello from Monster"
}