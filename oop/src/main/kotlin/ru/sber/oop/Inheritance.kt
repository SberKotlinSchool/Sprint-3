package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    private val monster: Monster = Goblin("power", 10, "Gob1", "goblin")

    constructor(name: String) :this(name = name, size = 100)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare : Room(name = "Town Square", size = 1000) {
    override val dangerLevel: Int
        get() = super.dangerLevel -3

    final override fun load(): String {
        return "Override load()"
    }
}
