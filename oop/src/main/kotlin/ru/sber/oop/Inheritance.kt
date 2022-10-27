package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here..."

}

class TownSquare : Room(name = "Town Square", size = 1000) {
    override val dangerLevel = super.dangerLevel - 3

    final override fun load(): String = "Loading..."


}
