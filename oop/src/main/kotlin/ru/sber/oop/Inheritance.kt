package ru.sber.oop

open class Room(
    val name: String,
    val size: Int,
    val monster: Monster = Goblin("Valera", "Green", "Super", 100)) {

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    constructor(name: String) : this(
        name,
        100
    )

}

class TownSquare : Room("Town Square", 1000) {
    final override fun load() = "loading..."
    override val dangerLevel = super.dangerLevel - 3
}