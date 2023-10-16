package ru.sber.oop

open class Room(private val name: String, val size: Int) {

    open val dangerLevel = 5
    private val monster = Goblin()

    constructor(name: String) : this(name, 100)


    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

class TownSquare(name: String, size: Int = 1000) : Room(name, size) {
    override val dangerLevel = 2
    final override fun load(): String {
        return "Something new string"
    }
}

fun Monster.getSalutation():String{
    return "Hi Monster!!"
}
