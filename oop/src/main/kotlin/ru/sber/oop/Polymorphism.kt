package ru.sber.oop

import kotlin.properties.Delegates
import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    var damageRoll: Int

    fun damageRoll(): Int {
        return Random.nextInt()
    }

    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...
class Player(val name: String, var isBlessed: Boolean, override var powerType: String, override var healthPoints: Int, override var damageRoll: Int): Fightable{

    override fun attack(opponent: Fightable): Int {
        when(isBlessed){
            false -> damageRoll = damageRoll()
            true -> damageRoll = damageRoll()*2
        }
        healthPoints -= damageRoll
        return damageRoll
    }
}

abstract class Monster(val name: String, val description: String): Fightable{

    override fun attack(opponent: Fightable): Int {
        return damageRoll().also { damageRoll = it }
    }
}

class Goblin(name: String, description: String, override val powerType: String,
             override var healthPoints: Int, override var damageRoll: Int): Monster(name, description) {

    override fun damageRoll(): Int {
        return super.damageRoll() / 2
    }
}