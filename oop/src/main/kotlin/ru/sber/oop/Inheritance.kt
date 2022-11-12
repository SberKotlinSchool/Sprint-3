package ru.sber.oop

open class Room(val name: String, val size: Int) {
    protected open val dangerLevel = 5
    private val monster: Monster = Goblin("Ug Gluk", "Vary bad guy", "Power/2", 18)

    constructor(name: String): this(name, 100)

    fun description() = "Room: $name"

    private fun Monster.getSalutation() = "Hey guy! I don't like your face!!"
    open fun load() = "${monster.getSalutation()} Nothing much to see here..."
}



class TownSquare: Room(name = "Town Square", size = 1000) {
    override val dangerLevel get() = super.dangerLevel - 3
    final override  fun load() = "And nothing else matters..."
}

/*
fun main() {
    val room = Room("Room1", 100)
    println(room.load())

    val square = TownSquare()
    println(square.load())
}
 */