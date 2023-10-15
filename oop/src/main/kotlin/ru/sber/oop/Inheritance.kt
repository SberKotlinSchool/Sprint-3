package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    constructor(name: String) : this(
        name,
        100
    )

    val monster = Goblin("Goblin", "monster", "goblining", 1000)
}

fun Monster.getSalutation() = "Hello ${this.name}"

class TownSquare : Room("Town Square", 1000) {
    final override fun load() = "measure seven times cut once"
    override val dangerLevel = super.dangerLevel - 3
}