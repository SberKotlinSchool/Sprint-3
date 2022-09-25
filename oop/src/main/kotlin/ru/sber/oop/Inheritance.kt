package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(size = 100, name = name)

    protected open val dangerLevel = 5
    val monster: Monster = Goblin(
        "Goblin power",
        500,
        "Goblin Weak",
        "Goblin 1"
        )

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()
}

//TODO: create class TownSquare here...
open class TownSquare: Room(name = "Town Square", size = 1000) {

    override final fun load() = "Loaded Town Square"

    override val dangerLevel = super.dangerLevel - 2
}
