package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    val monster: Monster = Goblin(
        name = "Goblin Junior",
        description = "The smallest goblin",
        powerType = "Fire",
        healthPoints = 100)

    constructor(name:String) : this(name = name, size = 100)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

class TownSquare(name: String = "Town Square", size: Int = 1000): Room(name, size) {

    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "This is Town Square"
}
