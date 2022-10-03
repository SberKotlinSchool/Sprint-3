package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = (0..100).random()

    fun attack(opponent: Fightable): Int
}

class Player(override val powerType: String,
             override var healthPoints: Int,
             val name: String,
             val isBlessed: Boolean
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed)
            this.damageRoll * 2
        else
           this.damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(val name: String,
                       val description: String
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= this.damageRoll
        return this.damageRoll
    }
}

class Goblin(name: String,
             description: String,
             override val powerType: String,
             override var healthPoints: Int
) : Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation() {
    println("Bonjour!")
}


