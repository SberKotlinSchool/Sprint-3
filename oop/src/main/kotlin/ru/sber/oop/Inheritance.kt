package ru.sber.oop

open class Room(var name: String, open val size: Int) {
    constructor(name: String) : this(
        name = name,
        size = 100
    )

    protected open val dangerLevel = 5
    var monster: Monster = Goblin()

    fun description() = "Room: $name, Size : $size"

    open fun load() = monster.getSalutation()
}

class TownSquare : Room("Town Square", 1000) {
    final override fun load() = "Hello from Town Square"
    override val dangerLevel = super.dangerLevel - 3
}
