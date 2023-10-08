package ru.sber.oop

open class Room(val name: String, val size: Int) {

    private val goblin: Monster = Goblin("powerType", 100, "goblin", "goblin's desc")

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here... ${goblin.getSalutation()}"

}

class TownSquare : Room("Town Square", 1000) {

    override fun load() = "TownSquare's load"

    override val dangerLevel = super.dangerLevel - 3
}
