package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(val name: String, val isBlessed: Boolean) : Fightable {

    override val powerType: String
        get() = TODO("Not yet implemented")

    override var healthPoints: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun attack(opponent: Fightable): Int {
        return if (isBlessed) damageRoll * 2 else damageRoll
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        return damageRoll
    }
}

class Goblin : Monster(name = "Goblin", description = "Scary monster") {

    override val powerType: String
        get() = TODO("Not yet implemented")

    override var healthPoints: Int
        get() = TODO("Not yet implemented")
        set(value) {}

    override val damageRoll: Int
        get() = super.damageRoll / 2
}