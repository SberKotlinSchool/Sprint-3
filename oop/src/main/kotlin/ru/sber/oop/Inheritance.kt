package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    protected open fun load() = "Nothing much to see here..."

    var monster: Monster = Goblin("Range", 200, 10, "Imsh", "Green and ugly")

}

//TODO: create class TownSquare here...
open class TownSquare : Room("Town Square", 1000) {
    final override fun load(): String {
        monster.getSalutation()
        return "Seeing all nearby monsters"
    }

    override val dangerLevel = 2
}

fun main() {
    var r = Room("A", 1)
}

fun Monster.getSalutation() = print("Приветствие монстра...")