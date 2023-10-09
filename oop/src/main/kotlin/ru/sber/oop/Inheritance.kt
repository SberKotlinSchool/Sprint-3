package ru.sber.oop

open class Room(val name: String, val size: Int) {
    constructor(name: String) : this(name, size = 100)

    private val goblin: Monster = Goblin("Goblin", "Monster Goblin")

    protected open val dangerLevel = 5
    fun description() = "Room: $name"
    open fun load() = goblin.getSalutation()
}

open class TownSquare(name: String = "Town Square", size: Int = 1000) : Room(name, size) {

    override val dangerLevel = super.dangerLevel - 3
    final override fun load() = "Nothing much to see here...too..."
}