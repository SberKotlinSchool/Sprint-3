package ru.sber.oop
open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    val monster : Monster = Goblin(name = "George",
        description = "ordinary Goblin",
        powerType = "shape shift",
        healthPoints = 100)

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() : String {
        this.monster.getSalutation()
        return "Nothing much to see here..."
    }
}


open class TownSquare() : Room(name = "Town Square", size = 1000) {
    final override fun load() = "So much to see here!"
    override val dangerLevel: Int
        get() = super.dangerLevel - 3
}