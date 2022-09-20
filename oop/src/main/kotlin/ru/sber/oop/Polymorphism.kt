package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = (0..10).random() * 2

    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    private val isBlessed: Boolean
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        return if (isBlessed) {
            opponent.healthPoints -= 2 * damageRoll
            2 * damageRoll
        } else {
            opponent.healthPoints -= damageRoll
            damageRoll
        }
    }
}

abstract class Monster(
    val name: String,
    val description: String
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= 2 * damageRoll
        return 2 * damageRoll
    }

}


class Goblin(
    override val powerType: String,
    override var healthPoints: Int,
    name: String,
    description: String
) : Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2

}

fun Monster.getSalutation(): String = "Za ordu!"
