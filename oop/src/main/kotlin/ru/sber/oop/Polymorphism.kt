package ru.sber.oop

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int

    fun attack(opponent: Fightable): Int
}


class Player(val name: String, var isBlessed: Boolean) : Fightable {
    override val powerType = "Player Power"
    override var healthPoints = 100

    override val damageRoll: Int
        get() = (Math.random() * 20).toInt() // Чтение по умолчанию для damageRoll

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}


abstract class Monster(val name: String, val description: String) : Fightable {
    override val powerType = "Monster Power"
    override var healthPoints = 100

    abstract override val damageRoll: Int

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin : Monster("Goblin", "A small and cunning creature") {
    override val damageRoll: Int
        get() = (Math.random() * 10).toInt() // Переопределение damageRoll для Goblin
}

class RoomNew() {

    private val monster: Monster = Goblin()

    fun description(): String {
        return "Monster: ${monster.name}\n" + "Description: ${monster.description}"
    }

    fun load(): String {
        return monster.getSalutation()
    }
}

fun Monster.getSalutation(): String {
    return "Hello, I am $name. $description"
}
