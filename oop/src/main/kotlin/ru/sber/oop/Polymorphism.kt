package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = (1..10).random()

    fun attack(opponent: Fightable): Int
}

class Player(
    val name: String,
    val isBased: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = if (isBased) 2 * damageRoll else damageRoll
        opponent.healthPoints -= damage
        return damage
    }

    override fun toString() = "$name[$healthPoints]"
}

abstract class Monster(
    val name: String,
    val description: String
) : Fightable {
    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }

    override fun toString() = "$name $description[$healthPoints]"
}

class Goblin(
    name: String, description: String, override val powerType: String, override var healthPoints: Int
) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun main() {
    val player = Player("player1", true, "power", 100)
    val monster: Monster = Goblin("goblin1", "monster", "monsterPower", 100)

    while (player.healthPoints > 0 && monster.healthPoints > 0) {
        player.attack(monster)
        monster.attack(player)
        println("$player\t$monster")
    }
    println(if (player.healthPoints > 0) "Player win" else "Monster win")
}