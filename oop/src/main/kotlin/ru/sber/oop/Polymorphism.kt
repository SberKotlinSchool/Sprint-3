package ru.sber.oop

import kotlin.random.Random


//Первоначальные настройки
interface Fightable {
    val powerType: String
    var healthPoints: Int

    val damageRoll: Int
        get() = Random.nextInt()
    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...

class Player (override val powerType: String, override var healthPoints: Int,
              val name: String,var isBlessed: Boolean) : Fightable {
                        override fun attack(opponent: Fightable): Int {
                            if (!isBlessed) {
                                healthPoints -= damageRoll
                                if (healthPoints <=0) healthPoints = 0
                                return damageRoll
                            }
                            else {
                                healthPoints -= (damageRoll *2)
                                if (healthPoints <=0) healthPoints = 0
                                return (damageRoll *2)
                            }
    }
}

abstract class Monster (val name: String, val description: String): Fightable {

    override fun attack(opponent: Fightable): Int {
            healthPoints -= damageRoll
            if (healthPoints <=0) healthPoints = 0
            return damageRoll
        }

}

class Goblin(name: String, description: String,
             override val powerType: String, override var healthPoints: Int) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2

}

