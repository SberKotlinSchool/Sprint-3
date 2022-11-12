package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Math.random().toInt()
    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...

class Player(override val powerType: String,
             override var healthPoints: Int,
             override val damageRoll: Int,
             val name : String,
             private val isBlessed : Boolean
             ) : Fightable{
    override fun attack(opponent: Fightable): Int {
        return damageRoll * (1 + if (isBlessed) 1 else 0)
    }
}

abstract class Monster(val name : String, val description : String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }

    fun Room.getSalutation(){
        println("I'am $name")
        load()
    }
}

class Goblin(override val powerType: String,
             override var healthPoints: Int,
             name: String,
             description: String
)
    : Monster(name, description) {

    override fun attack(opponent: Fightable): Int {
        return super.attack(opponent) / 2
    }
}