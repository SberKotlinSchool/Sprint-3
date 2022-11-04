package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(100)

    fun attack(opponent: Fightable): Int
}

class Player(override val powerType: String,
             override var healthPoints: Int,
             val name: String,
             var isBlessed: Boolean = false) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= damage
        return damage;
    }
}

sealed class Monster(override val powerType: String,
                     override var healthPoints: Int,
                     val description: String,
                     val name: String) : Fightable {
     override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin : Monster(name = "Goblin", description = "Default goblin enemy", healthPoints = 100, powerType = "Knife") {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}


//TODO: create class Player, Monster, Goblin here...


