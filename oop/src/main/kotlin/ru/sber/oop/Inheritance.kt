package ru.sber.oop

open class Room(
        val name: String,
        val size: Int,
        val monster : Monster = Goblin("fire", 10, "lil goblin", "low-level monster")
) {

    constructor(name : String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare : Room("Town Square", 1000) {
    final override fun load(): String = "Town Square have so much room"

    override val dangerLevel: Int
        get() = super.dangerLevel - 3
}

private fun Monster.getSalutation() = "Howdy"
