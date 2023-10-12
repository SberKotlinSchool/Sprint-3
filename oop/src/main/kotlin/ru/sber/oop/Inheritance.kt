package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    val monster: Monster = Goblin("Piercing", 30, "Kek", "Skinny")

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    private fun Monster.getSalutation() = "Die, human! I'm ${monster.name}!"
}

class TownSquare(name: String = "Town Square", size: Int = 1000) : Room(name, size) {

    public override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load() = "There is something here."
}

fun main() {
    val square: Room = TownSquare()
    println(square.load())
    val room = Room("Just room.")
    println(room.load())
    val player = Player("Crushing", 40, "Hero", true)
    println("Player hits: " + player.attack(room.monster))
    println("Monster hits remaining: " + room.monster.healthPoints)
    println("Monster hits: " + room.monster.attack(player))
    println("Player hits remaining: " + player.healthPoints)
}