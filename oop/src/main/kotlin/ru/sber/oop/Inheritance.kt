package ru.sber.oop

val LOAD_ROOM_RESULT = "Loading the room..."

open class Room(val name: String, val size: Int) {

    val monster: Monster = Goblin("SuperGoblin", 80, "Gobly", "Little goblin")

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = LOAD_ROOM_RESULT
}

class TownSquare() : Room("TownSquare", 1000) {

    override val dangerLevel: Int = super.dangerLevel - 3

    final override fun load() = "In TownSquare is sitting the monster. And he says: \"" + monster.getSalutation() + "\""

}


fun main() {
    val room: Room = TownSquare()
    println(room.load())
}