package ru.sber.oop

open class Room(
    private val name: String,
    private val size: Int
) {

    constructor(name: String) : this(name, 100)

    private val monster = Goblin("Crork", "Son of Pudragg", "desert goblin", 300)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here... Monster screams - ${monster.getSalutation()}"

}

class TownSquare : Room("Town Square", 1000) {
    override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load(): String {
        return "Town Square loading..."
    }
}
