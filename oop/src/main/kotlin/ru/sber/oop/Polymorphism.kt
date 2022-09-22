package ru.sber.oop

import kotlin.math.min
import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        // Добавили метод чтения по умолчанию
        // Пусть будет произвольное число до 100
        get() = Random.nextInt(0, 100)

    fun attack(opponent: Fightable): Int
}

class Player(
    val name: String,
    override val powerType: String,
    override var healthPoints: Int,
    var isBlessed: Boolean = false
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val result = min(opponent.healthPoints, (this.damageRoll * (if (!isBlessed) 1 else 2)))
        opponent.healthPoints -= result
        return result
    }
}

open class Monster(
    val name: String,
    override val powerType: String,
    override var healthPoints: Int = 100
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val result = min(opponent.healthPoints, this.damageRoll)
        opponent.healthPoints -= result
        return result
    }
}

// Добавили функцию-расширение с приветствием
fun Monster.getSalutation() = "Я, ${this.name}, приветствую тебя!"

class Goblin(name: String, powerType: String, healthPoints: Int) : Monster(name, powerType, healthPoints) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

// Игра "Кто кого победит"
fun main() {
    val player = Player("Игрок", "Огнемёт", 100)
    val monster = Monster("Злобный монстр", "Острые когти")

    /*
    Битва началась. Монстр начинает первым,
    но на каждый второй удар силы игрока удваиваются
    */
    var counter = 0
    while (player.healthPoints > 0 && monster.healthPoints > 0) {
        println("${monster.name} атаковал: ${monster.attack(player)}")
        if (player.healthPoints == 0) {
            println("${monster.name} победил")
            break
        }

        player.isBlessed = (++counter % 2 == 0)

        println("${player.name} атаковал: ${player.attack(monster)}")
        if (monster.healthPoints == 0) {
            println("${player.name} победил")
            break
        }
    }
}
