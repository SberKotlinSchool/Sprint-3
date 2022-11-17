package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String): this(name, 100)

    protected open val dangerLevel = 5
    var monster: Monster = Goblin()
    fun description() = "Room: $name"

    open fun load(): String {
        return String.format("Nothing much to see here... %s", monster.getSalutation())
    }

}

class TownSquare(name: String = "Town Squere", size: Int = 1000): Room(name, size) {

    override val dangerLevel get() = super.dangerLevel - 3
    final override fun load() = "Rise and shine, Mr.Freeman..."
}

fun Monster.getSalutation(): String {
    return this.description
}