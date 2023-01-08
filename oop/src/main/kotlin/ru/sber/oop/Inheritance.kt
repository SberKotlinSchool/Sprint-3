package ru.sber.oop

open class Room(val name: String, val size: Int) {
    private val monster: Monster = Goblin("Goblin", "Green", "Smell", 1)
    protected open val dangerLevel = 5

    constructor() : this("Room", 100)

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here... ${monster.getSalutation()}"

}

//TODO: create class TownSquare here...
open class TownSquare : Room("Town Square", 1000) {
    final override fun load() = "Load Town Square"
    override val dangerLevel: Int
        get() = super.dangerLevel - 3
}

private fun Monster.getSalutation() = "Hello, ${this.name}!"