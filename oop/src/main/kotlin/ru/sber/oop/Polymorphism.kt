package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(10, 100)

    fun attack(opponent: Fightable): Int
}

class Player(val name: String = "Player1", val isBlessed: Boolean = false,
             override val powerType: String, override var healthPoints: Int) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val loss = if (isBlessed) {
            2 * damageRoll
        } else {
            damageRoll
        }
        opponent.healthPoints -= loss
        return loss
    }
}

abstract class Monster(val name: String = "Mike Wazowski", val description: String = "Monsters, Inc",
             override val powerType: String, override var healthPoints: Int) : Fightable {
    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin(override val powerType: String, override var healthPoints: Int)
    : Monster("Goblin", "Goblins, Inc", powerType, healthPoints){
     override val damageRoll: Int
               get() = super.damageRoll/2
}


