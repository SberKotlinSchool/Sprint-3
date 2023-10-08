package ru.sber.oop

open class Room(
    val name: String,
    val size: Int
) {
    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    private val monster: Monster = Goblin("Goblin", "Description")

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

//TODO: create class TownSquare here...
class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel: Int = 3

    override fun load(): String {
        return "Hello from TownSquare"
    }
}
