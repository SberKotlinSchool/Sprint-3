package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String

    // как в базовом интерфейсе healthPoints может быть контантой,
    // если во всех дочках мы её меняем. Переделал.
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

private class Player(_name: String,
                     _isBlessed: Boolean,
                     _hp: Int = 100,
                     _powerType: String = "Holy") : Fightable {

    val name: String = _name
    val isBlessed: Boolean = _isBlessed
    override val powerType: String = _powerType
    override var healthPoints: Int = _hp

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll * if (isBlessed) 1 else 2
        opponent.healthPoints -= damage
        return  damage
    }
}

open class Monster(_name: String,
                   _description: String,
                   _hp: Int = 100,
                   _powerType: String = "Normal") : Fightable {
    val name: String = _name
    val description: String = _description
    override val powerType: String = _powerType
    override var healthPoints: Int = _hp

    override fun attack(opponent: Fightable): Int {
        val damage =  damageRoll
        opponent.healthPoints -= damage
        return  damage
    }
}


class Goblin : Monster("Goblin", "Weakest monster") {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

val Room.monster: Goblin
    get() = Goblin()

fun Monster.getSalutation(): String ="Hi, im $name"
