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
class Player(val name: String, var isBlessed: Boolean): Fightable{
    override lateinit var powerType: String
    override var healthPoints by Delegates.notNull<Int>()
    override var damageRoll by Delegates.notNull<Int>()

    override fun attack(opponent: Fightable): Int {
        when(isBlessed){
            false -> damageRoll = damageRoll()
            true -> damageRoll = damageRoll()*2
        }
        return damageRoll
    }
}

abstract class Monster: Fightable{
    abstract val name: String
    abstract val description: String

    override fun attack(opponent: Fightable): Int {
        return damageRoll().also { damageRoll = it }
    }

    fun getSalution() = "Hey, I'm a Monster"

}

class Goblin: Monster() {
    override lateinit var name: String
    override lateinit var description: String
    override lateinit var powerType: String
    override var healthPoints by Delegates.notNull<Int>()
    override var damageRoll by Delegates.notNull<Int>()

    override fun damageRoll(): Int {
        return super.damageRoll() / 2
    }

}
