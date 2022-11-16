package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() {
            return (1..10).random()
        }

    fun attack(opponent: Fightable): Int
}

class Player(val name: String, val isBlessed: Boolean, override val powerType: String, override var healthPoints: Int) :
    Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= damage
        if (opponent.healthPoints < 0) {
            opponent.healthPoints = 0
        }
        return damage
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        if (opponent.healthPoints < 0) {
            opponent.healthPoints = 0
        }
        return damage
    }
}

class Goblin(name: String, description: String, override val powerType: String, override var healthPoints: Int) :
    Monster(name, description) {
    override val damageRoll: Int
        get() {
            return super.damageRoll / 2
        }
}

//fun main() {
//    val player1 = Player("player1", true, "", 100)
//    val player2 = Player("player2", true, "", 100)
//    player1.attack(player2)
//    println("${player2.name}, current health = " + player2.healthPoints)
//
//    val goblin1 = Goblin("player1", "some goblin", "", 100)
//}


