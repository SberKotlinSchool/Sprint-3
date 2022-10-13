package ru.sber.oop

 open class Room(val name: String, val size: Int) {

    open val dangerLevel = 5
    fun description() = "Room: $name"
    open fun load() = monster.getSalution()

     constructor(name: String) : this(size = 100, name = name)

     val monster = Goblin("Goblin", 100, "Goblin", "Goblin")
}


//TODO: create class TownSquare here...
class TownSquare : Room("Town Square", 1000){
    final override fun load() = "Town Square loading..."
    override val dangerLevel = super.dangerLevel -2

}

fun main() {
    val townSquare = TownSquare()
    println(townSquare.load())
    val room = Room("box", 100)
    println(room.load())

}


