package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    val monster = Goblin("BigFoot", "Goblin", "Normal", 1000)

}

class TownSquare : Room("Town Square", 1000) {
    final override fun load() = "Load"
    override val dangerLevel = super.dangerLevel - 3

}
