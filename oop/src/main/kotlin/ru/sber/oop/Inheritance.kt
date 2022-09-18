package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    private fun Fightable.Monster.getSalutation(): String = "Hello, Monster"
    open fun load() = monster.getSalutation()

    private val monster : Fightable.Monster = Fightable.Goblin("Cute",100,"Cat","Cartoon Cat")

}

//TODO: create class TownSquare here...
class TownSquare: Room("Town Square", 1000){
    final override fun load(): String {return "there is something to see on Town Square"}
    override val dangerLevel = super.dangerLevel -3
}
