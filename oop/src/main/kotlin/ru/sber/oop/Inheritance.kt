package ru.sber.oop

fun Monster.getGreetingsStr() = "HE-HE HA-HA!"

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    private val monster = Goblin()

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here...  ${monster.getGreetingsStr()}"

}

class TownSquare : Room("TownSquare", 1000) {

    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "Loading TownSquare room..."

}
