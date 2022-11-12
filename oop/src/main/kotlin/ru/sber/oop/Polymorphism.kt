package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int  get() = Random((Math.random() * 100).toInt()).nextInt(1, 6) // типа кубик

    fun attack(opponent: Fightable): Int

}

class Player(val name: String, val isBlessed: Boolean, override val powerType: String, override var healthPoints: Int) : Fightable {
    override fun attack(opponent: Fightable): Int {
        var damage: Int = if (isBlessed) damageRoll *2 else damageRoll
        opponent.healthPoints -= damage
        if (opponent.healthPoints < 0) opponent.healthPoints = 0
        return damage
    }
}

abstract class Monster(val name: String, val description: String): Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage: Int = damageRoll
        opponent.healthPoints -= damage
        if (opponent.healthPoints < 0) opponent.healthPoints = 0
        return damage
    }
}

class Goblin(name: String, description: String, override val powerType: String, override var healthPoints: Int): Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

/*
fun main() {
    val player = Player("Angry knight", false, "Power", 20)
    val goblin = Goblin("Ug Gluk", "Vary bad guy", "Power/2", 18)
    var playerDamage : Int
    var goblinDamage : Int

    println("Ready? FIGHT!!!!")
    while ((player.healthPoints > 0) && (goblin.healthPoints > 0)) {
        if (player.healthPoints > 0) {
            playerDamage = player.attack(goblin)
            println("The player ${player.name} attacked monster ${goblin.name} and dealt ${playerDamage} damage, monster health ${goblin.healthPoints}" )
        }
        if (goblin.healthPoints > 0) {
            goblinDamage = goblin.attack(player)
            println("The monster ${goblin.name} attacked player ${player.name} and dealt ${goblinDamage} damage, player health ${player.healthPoints}" )
        }
    }
    println("Battle is over.")
    if (player.healthPoints > 0) println("Player ${player.name} WIN!!!")
    if (goblin.healthPoints > 0) println("Monster ${goblin.name} WIN!!!")
}
*/