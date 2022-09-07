package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = (0..100).random()

    fun attack(opponent: Fightable): Int
}

class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed: Boolean
) : Fightable {

    override fun attack(opponent: Fightable): Int =
        if (isBlessed) {
            opponent.healthPoints -= 2 * damageRoll
            2 * damageRoll
        } else {
            opponent.healthPoints -= damageRoll
            damageRoll
        }
}

abstract class Monster(
    val name: String,
    val description: String
) : Fightable {


    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
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

fun Goblin.getSalutation(): String = "Агрхх!"



