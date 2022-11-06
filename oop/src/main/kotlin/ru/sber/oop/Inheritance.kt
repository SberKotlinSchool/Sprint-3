package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    val monster: Monster = Goblin("dirt", 40)

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

class TownSquare() : Room("Town Square", 1000) {

    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "Overridden for TownSquare"

}
