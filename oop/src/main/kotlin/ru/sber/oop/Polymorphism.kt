package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    open val damageRoll: Int
        get() = Random.nextInt()
    fun attack(opponent: Fightable): Int
}
class Player : Fightable{

    override val powerType: String = "Player"
    override var healthPoints: Int = 100
    val name: String = ""
    val isBlessed: Boolean = false
    override fun attack(opponent: Fightable): Int {
        val damage: Int = damageRoll + if (isBlessed) damageRoll else 0
        opponent.healthPoints - damage
        return damage
    }
}

abstract class Monster: Fightable{
    val name: String = ""
    val description: String = "Monster"
    override fun attack(opponent: Fightable): Int {
        val damage: Int = damageRoll
        opponent.healthPoints - damage
        return damage
    }
}

class Goblin: Monster(){

    override val powerType: String = "NPC"
    override var healthPoints: Int = 200
    override val damageRoll: Int
        get() = Random.nextInt() / 2

}


