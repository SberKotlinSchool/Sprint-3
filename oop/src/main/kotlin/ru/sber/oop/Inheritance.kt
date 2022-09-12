package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    private val monster: Monster = Goblin(name = "Goblin Petya", powerType = "Physic", healthPoints = 500)

    private fun Monster.getSalutation(): String {
        return "Monster $name, salutation to you :)"
    }

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here...\n ${monster.getSalutation()}"

    constructor(_name: String) : this(_name, 100) {

    }

}

//TODO: create class TownSquare here...
open class TownSquare() : Room("Town Square", 1000) {
    final override fun load() = "Nothing much to see on Town Square"

    public override val dangerLevel: Int
        get() = super.dangerLevel - 3
}

////testDrive
//fun main() {
//    val room = Room("room1", 10)
//    val townSquare = TownSquare()
//    println(townSquare.dangerLevel)
//    println(room.load())
//}