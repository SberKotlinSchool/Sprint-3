package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    constructor(name: String) : this(name, size = 100) {

    }
    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here... just loading " + monster.getSalutation()

    val monster: Monster = Goblin()

}

//TODO: create class TownSquare here...
class TownSquare (name: String = "Town Square", size: Int = 1000) : Room(name, size) {
    override val dangerLevel = 3
    override fun load() = "Nothing much to see on TownSquare..."
}

fun main(){
    val room1 = Room("first room", 2)
    val room2 = Room("second room")

    val townSquare = TownSquare()
    println(room1.load())
}