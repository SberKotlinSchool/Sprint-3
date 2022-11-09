package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() { return Random.nextInt() }

    fun attack(opponent: Fightable): Int
}

class Player(val name: String,
             val isBlessed: Boolean,
             override val powerType: String,
             override var healthPoints: Int) : Fightable {

    override fun attack(opponent: Fightable): Int
    {
        val damage = if(isBlessed) damageRoll else damageRoll*2;
        opponent.healthPoints -= damage;
        return damage;
    }
}

open class Monster(val name: String,
             val description: String,
             override val powerType: String,
             override var healthPoints: Int) : Fightable {

    override fun attack(opponent: Fightable): Int {

        opponent.healthPoints -= damageRoll;
        return damageRoll;
    }
}

class Goblin(name: String, description: String, powerType: String, healthPoints: Int) :
    Monster(name, description, powerType, healthPoints) {

    override val damageRoll: Int
        get() = super.damageRoll/2

}

fun Monster.getSalutation(): String {
    return "You're gonna die from ${this.damageRoll} of ${this.name}"
}

