package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    private val monster: Monster = Goblin("Two-handed axe", 100, "Garrosh", "Warrior")

    fun description() = "Room: $name"

    open fun load(): String = monster.getSalutation()

    constructor(name: String) : this(name, 100)
}

class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel = super.dangerLevel - 3
    final override fun load(): String = "String for loading"
}