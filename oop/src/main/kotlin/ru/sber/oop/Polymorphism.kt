package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = (10..20).random()


    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...
class Player(
    override val powerType: String,
    override var healthPoints: Int,
    override val damageRoll: Int,
    val name: String,
    val isBlessed: Boolean,
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val dmg = damageRoll * if (isBlessed) 2 else 1
        opponent.healthPoints -= dmg
        return dmg
    }
}

abstract class Monster(
    override val powerType: String,
    override var healthPoints: Int,
    override val damageRoll: Int,
    open val name: String,
    open val desctiption: String,
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val dmg = damageRoll
        opponent.healthPoints -= dmg
        return dmg
    }
}

class Goblin(
    override val powerType: String,
    override var healthPoints: Int,
    override val damageRoll: Int,
    override val name: String,
    override val desctiption: String,
) : Monster(
    powerType, healthPoints, damageRoll, name, desctiption,
) {
    override fun attack(opponent: Fightable): Int {
        val dmg = damageRoll / 2
        opponent.healthPoints -= dmg
        return dmg
    }
}