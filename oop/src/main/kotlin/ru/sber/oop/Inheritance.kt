package ru.sber.oop

open class Room(open val name: String, open val size: Int) {
    private val goblin: Monster = Goblin("Goblin", "powerful and mighty", "monarchy", 150)


    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here... ${goblin.getSalutation()}"

    constructor(name: String) : this(name, 100)

}

open class TownSquare : Room("TownSquare", 1000) {
    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "It's a square in a town"
}