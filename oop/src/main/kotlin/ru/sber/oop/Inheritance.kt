package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = "Nothing much to see here..."

}

class TownSquare(name:  String) : Room(name, 1000){
    override fun load() = "parents must be open"
    override val dangerLevel: Int
        get() = super.dangerLevel - 3
}
