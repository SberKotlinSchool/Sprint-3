package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    val monster = Goblin("Сила", 25)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

class TownSquare(name: String = "Town Square", size: Int = 1000) : Room(name, size) {
    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "The Fog Is Coming..."
}

fun Monster.getSalutation() : String {
    return "Здравствуй, путник"
}
