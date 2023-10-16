package ru.sber.oop

open class Room(val name: String, val size: Int) {
    companion object {
        const val DEFAULT_SIZE_WITH_NAME = 100
    }

    val monster: Monster = Goblin(
        "Test", "Test", "Test", 100
    )

    constructor(name: String): this(name, DEFAULT_SIZE_WITH_NAME)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load(): String {
        monster.getSalutation()
        return "Nothing much to see here..."
    }

}

class TownSquare(): Room("Town square", 1000) {
    override val dangerLevel = super.dangerLevel - 3

    override fun load() = "$name has been loaded"
}

fun Monster.getSalutation() = println("Hello!")
//TODO: create class TownSquare here...
