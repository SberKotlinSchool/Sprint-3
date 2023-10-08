package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    private val monster = Goblin("Слабый", 50, "Гоша", "Гоблин")
    constructor(name: String): this(name, 100)
    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

class TownSquare(): Room("TownSquare", 1000) {
    override val dangerLevel: Int
        get() = super.dangerLevel - 3
    final override fun load() = "Просто загрузка"
}

fun  Monster.getSalutation() = "Привет, я дружелюбный монстр"
