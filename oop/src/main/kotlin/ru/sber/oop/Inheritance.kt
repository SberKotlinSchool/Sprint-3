package ru.sber.oop

open class Room(val name: String, val size: Int) {
    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load(): String = monster.getSalutation()

    private val monster = Goblin("Fire", 30, "Goblin", "Goblin")

}

//TODO: create class TownSquare here...
class TownSquare : Room("Town Square", 1000) {
    final override fun load(): String = "Town Square loading"
    override val dangerLevel = super.dangerLevel - 3
}