package ru.sber.oop

open class Room(
    val name: String,
    val size: Int
) {
    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    private val monster: Monster = Goblin(
        name = "Goblin",
        description = "Description",
        powerType = "Magic",
        healthPoints = 100
    )

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

//TODO: create class TownSquare here...
class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel: Int = super.dangerLevel - 3

    override final fun load(): String {
        return "Hello from TownSquare"
    }
}
