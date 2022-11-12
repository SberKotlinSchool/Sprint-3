package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this (name, 1000)

    protected open val dangerLevel = 5
    val monster : Monster = Goblin("Test",100, "Goblin","Goblin #1")

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here..."

}

//TODO: create class TownSquare here...

class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel = super.dangerLevel - 3
    override fun load() = "New string"
}