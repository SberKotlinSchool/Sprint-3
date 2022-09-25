package ru.sber.oop

import java.util.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random().nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(override val powerType: String, override var healthPoints: Int) : Fightable {
    var name : String? = null
    var isBlessed : Boolean = false

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll * if (isBlessed) 2 else 1
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(open val name : String, open val description : String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(override val name: String,
             override val description: String,
             override val powerType: String,
             override var healthPoints: Int) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation() : String {
    return "hello World from ${this.name}"
}

