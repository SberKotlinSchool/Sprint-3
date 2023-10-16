package ru.sber.oop

open class Room(private val name: String, val size: Int) {

    open val dangerLevel = 5
    private val monster = Goblin()

    constructor(name: String) : this(name, 100)


    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare(name: String = "Town Square", size: Int = 1000) : Room(name, size) {
    override val dangerLevel = super.dangerLevel - 3
    final override fun load(): String {
        return "Something new string"
    }
}

fun Monster.getSalutation(): String {
    return "Hi Monster!!"
}
