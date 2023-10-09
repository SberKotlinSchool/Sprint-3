package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(0, Int.MAX_VALUE)
    fun attack(opponent: Fightable): Int
}
class Player(val name: String, val isBlessed: Boolean) : Fightable {
    override val powerType: String
        get() = this.javaClass.interfaces.firstOrNull()?.simpleName ?: this.javaClass.simpleName
    override var healthPoints = Int.MAX_VALUE

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}
open class Monster(val name: String, val description: String) : Fightable {
    override val powerType: String
        get() = this.javaClass.interfaces.firstOrNull()?.simpleName ?: this.javaClass.simpleName
    override var healthPoints = Int.MAX_VALUE

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}
class Goblin(name: String, description: String) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation() = "Hello, i'm ${this.name}"