package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here..."

    constructor(name: String) : this(name, 100)

}

class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel: Int = super.dangerLevel - 3

    final override fun load() = "This is a bustling town square."

}
