package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt(0, 700)

    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...

class Goblin(
    override val name: String,
    override val description: String = "Weak monster",
    override val powerType: String,
    override var healthPoints: Int
) : Monster(), Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll / 2
        println("Attacking opponent for $damage")
        opponent.healthPoints -= damage
        return damage
    }

}

abstract class Monster : Fightable {
    abstract val name: String
    abstract val description: String

    /**
     * Метод позволяет монстру атаковать оппонента.
     */
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        println("Attacking opponent for $damage . Die insect!")
        opponent.healthPoints -= damage
        return damageRoll
    }

}

class Player(
    private val name: String,
    private val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {

    /**
     * Переопределенный метод attack, который позволяет игроку нанести критический урон.
     * Уменьшает здоровье оппоненту на damageRoll, если isBlessed = false, и удвоенный damageRoll, если isBlessed = true. Результат функции attack -
     * количество урона, которое нанес объект класса Player.
     */
    override fun attack(opponent: Fightable): Int {
        var damage = damageRoll

        if (isBlessed) {
            damage *= 2
        }

        println("$name attacking opponent for $damage. For the Glory!")
        opponent.healthPoints -= damage
        return damage

    }
}
//testDrive
//fun main() {
//    val player1 = Player("A1", true, "magic", 3000)
//    val player2 = Player("A2", false, "physic", 4000)
//
//    println(player2.healthPoints)
//    player1.attack(player2)
//    println(player2.healthPoints)
//}

