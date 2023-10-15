package ru.sber.oop

open class Room(val name: String, val size: Int) {
    protected open val dangerLevel = 5
    private val monster: Monster = Goblin("goblin", "monster description", "power", 100)

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here...${monster.getSalutation()}."

    override fun toString() = "Room(name=$name, size=$size, dangerLevel=$dangerLevel)"
}

class TownSquare : Room("Town Square", 1000) {
    override val dangerLevel = super.dangerLevel - 3
    override fun toString() = "TownSquare(name=$name, size=$size, dangerLevel=$dangerLevel)"
    final override fun load() = "Load TownSquare...${super.load()}"
}

fun Monster.getSalutation(): String = "Hi, I am Goblin! My name is $name."

fun main() {
    printAllAboutRoom(Room("room1", 1))
    printAllAboutRoom(TownSquare())
    printAllAboutRoom(Room("room2"))
}

private fun printAllAboutRoom(room: Room) {
    println("$room\n\tdescription: ${room.description()}\n\tload: ${room.load()}")
}