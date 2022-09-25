package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    private val monster = Goblin("Goblin", "goblin in the room", "horse power", 100)

    constructor(_name: String) : this(_name, 100)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare : Room("Town Square", 1000) {

    open override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load(): String = "Loading..."
}