package ru.sber.oop

open class Room(val name: String, val size: Int) {
    val monster = Goblin(
        name = "Goblin"
        , description = "monster"
        , powerType = "sword"
        , healthPoints = 100
    )

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()
}

class TownSquare(name: String) : Room(name, 1000) {
    final override fun load() = "parents must be open"
    override val dangerLevel: Int
        get() = super.dangerLevel - 3
}
