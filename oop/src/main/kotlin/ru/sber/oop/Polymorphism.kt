package ru.sber.oop

//import com.sun.org.apache.xpath.internal.operations.Bool
import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(100)

    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...
class Player(override val powerType: String,
             override var healthPoints: Int,
             val name: String,
             private val isBlessed: Boolean): Fightable {


    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        if (isBlessed) {
            opponent.healthPoints -= damage * 2
            return damage * 2
        }
        opponent.healthPoints -= damage

        return damage
    }
}

abstract class Monster(val description: String, val name: String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll

        opponent.healthPoints -= damage

        return damage
    }
}

open class Goblin(override val powerType: String = "Goblin power",
             override var healthPoints: Int = 1000,
             description: String,
             name: String): Monster(description, name) {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation(): String {
    return "\"Hello\" - Monster sayed"
}




