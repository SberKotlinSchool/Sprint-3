package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = enemy.getSalutation()

    constructor(name: String): this(name, 100)
}

class TownSquare: Room(name = "Town Square", size = 1000) {

    override val dangerLevel = super.dangerLevel - 3
    final override fun load() = "SimpleText"
}
