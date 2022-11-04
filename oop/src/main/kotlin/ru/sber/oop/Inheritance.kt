package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    private val monster: Monster = Goblin("Phis", 1000, "Goblin", "Ugly goblin")
    constructor(_name: String) : this(name = _name, size = 100)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()
}

class TownSquare : Room("Town Square", 1000) {
    override val dangerLevel: Int = 2

    override fun load() = "Everything is nothing"
}
