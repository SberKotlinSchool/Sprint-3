package ru.sber.oop

open class Room(val name: String, val size: Int) {
    protected open val dangerLevel = 5

    private val monster: Monster = Goblin(name = "F Goblin",
            description = "Goblin description",
            powerType = "Goblin power type",
            healthPoints = 100)

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() : String {
        this.monster.greetings()
        return "Nothing much to see here..."
    }

    fun Monster.greetings() {
        println("Cheer")
    }
}

open class TownSquare() : Room(name = "Town Square", size = 1000) {
    final override fun load() = "Town Square!"
    override val dangerLevel: Int = super.dangerLevel - 3
}