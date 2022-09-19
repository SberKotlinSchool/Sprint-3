package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(_name: String) : this(_name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here..."

}

open class TownSquare : Room("Town Square", 1000) {

    open override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load(): String = "Loading..."
}