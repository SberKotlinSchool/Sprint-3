package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    val monster: Monster = Goblin("Power3000", 100, "Monster1", "Description")

    fun description() = "Room: $name"

    open fun load() : String {
        monster.getSalutation()
        return "Nothing much to see here..."
    }

    constructor(name: String) : this(name, 100 )

    fun Monster.getSalutation() {
        println("Salut!")
    }

}

class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "Town Square is loading..."
}