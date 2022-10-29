package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(size = 100, name = name)

    protected open val dangerLevel = 5
    private val monster = Goblin("word", 100, "Puchkoff", "Lads and Ring")

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    override fun toString() : String {
        return "Room (name='$name', size=$size, dangerLevel=$dangerLevel)"
    }

    private fun Monster.getSalutation() : String {
        return "I'm a ${monster.name}, my power is ${monster.powerType}!"
    }
}

class TownSquare: Room(name = "Town Square", size = 1000) {

    override val dangerLevel: Int get() = super.dangerLevel - 3

    override fun load() : String {
        return "Town Square is a place of culture and relax"
    }

    override fun toString() : String {
        return "TownSquare (name='$name', size=$size, dangerLevel=$dangerLevel)"
    }
}

fun main() {
    val room = Room("Room1")
    println(room.toString())
    println(room.description())
    println(room.load())
    val square = TownSquare()
    println(square.toString())
    println(square.description())
    println(square.load())
}

