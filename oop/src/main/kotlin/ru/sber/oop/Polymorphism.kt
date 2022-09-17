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

class Player(override val powerType: String, override var healthPoints: Int, val name: String) : Fightable {

    val isBlessed: Boolean = false
    override fun attack(opponent: Fightable): Int {
        return if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
    }
}

abstract class Monster(val name: String, val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        return damageRoll
    }
}

class Goblin(override val powerType: String, override var healthPoints: Int, name: String, description: String) :
    Monster(name, description) {
    constructor() : this("", 1000, "boss", "some description")

    override val damageRoll: Int
        get() = Random.nextInt() / 2
}

fun Monster.getSalutation() = "Hello Player!"

fun main(){
    val player = Player("", 1, "demoPlayer")
    println(player.attack(player))
}