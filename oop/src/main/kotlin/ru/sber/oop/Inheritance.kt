package ru.sber.oop

open class Room(
    val name: String,
    val size: Int,
    var monster: Monster = Goblin("Melee", 100, "Guu", "Hobgoblin")
) {
    constructor(name : String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    protected open fun load() {
        monster.getSalutation()
    }
}

open class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel = 2
}

fun Monster.getSalutation() = "Hello, my little boya..."
