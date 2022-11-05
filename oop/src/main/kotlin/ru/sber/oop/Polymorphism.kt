package ru.sber.oop

import java.util.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random().nextInt()

    fun attack(opponent: Fightable): Int
}

class Player(
    val name: String,
    override var healthPoints: Int = 100,
    var isBlessed: Boolean = false
) : Fightable {
    override val powerType: String
        get() = "SuperPower"

    override fun attack(opponent: Fightable): Int {
        if (this.isBlessed)
            return opponent.healthPoints - this.damageRoll * 2
        else {
            return opponent.healthPoints - this.damageRoll
        }
    }
}

abstract class Monster(
    val name: String,
    val description: String
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        return opponent.healthPoints - this.damageRoll * 2
    }
}

fun Monster.getSalutation(): String = "GANG YARM BUDU!"

class Goblin(
    name: String = "Goblin",
    description: String = "An ugly  and angry creature",
) : Monster(name, description) {
    override val damageRoll: Int
        get() = (Random().nextInt()) / 2
    override val powerType: String
        get() = "ugliness"
    override var healthPoints: Int
        get() = 45
        set(value) {}
}





