package ru.sber.oop

open class Room(val name: String, val size: Int) {
    var monster : Monster = Goblin("Earth", 50, "Gobbo", "just Gobbo")
    protected open val dangerLevel = 5

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() = "You hear " + monster.getSalutation()

}

 open class TownSquare : Room("Town square", 1000){
     override val dangerLevel: Int
         get() = super.dangerLevel - 3

     @Override
     final override fun load() = "You enter the Town square"
 }

fun Monster.getSalutation() : String{
    return "Hello, my name is $name"
}
