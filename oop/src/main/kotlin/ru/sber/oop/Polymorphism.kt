package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

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
        var attack: Int = opponent.damageRoll
        if (isBlessed) {
            attack *= 2
        }
        opponent.healthPoints -= attack
        return attack
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        return opponent.damageRoll
    }
}

fun Monster.getSalutation():String {
    return "hello! I'm " + this.name
}

class Goblin(name: String
             , description: String
             , override val powerType: String
             , override var healthPoints: Int
             ) : Monster(name, description)
{
    override val damageRoll: Int
        get() = super.damageRoll/2
}