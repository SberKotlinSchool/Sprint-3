package ru.sber.oop

open class Room(val name: String, val size: Int) {
    constructor(name: String): this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    val monster: Monster = Goblin(
        name = "Waaagh",
        description = "Waaaagh?",
        powerType = "Waaaaaaaaagh!!!",
        healthPoints = 100500
    )

    open fun load() = String.format("Nothing much to see here, %s", monster.getSalutation())
}

open class TownSquare : Room(name = "Town Square", size = 1000) {
    override val dangerLevel get() = super.dangerLevel - 3

    final override fun load() = "Loading..."
}