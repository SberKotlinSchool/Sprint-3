package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation();

    val monster = Goblin("Azog", "Orc-lord of the Third Age", "Sword", 100)

    constructor(name: String) : this(name, 100)
}

class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel = super.dangerLevel - 3;

    final override fun load() = "All we have to decide is what to do with the time that is given us."

}
