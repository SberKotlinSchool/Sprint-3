package ru.sber.oop

open class Room(val name: String, val size: Int) {

    var monster = Goblin("Merlin", "something", "BruteForce", 1000)

    constructor(name: String): this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare() : Room(name = "Town Square", size = 1000) {

    override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load() = "придумайте строку для загрузки"

}

fun Monster.getSalutation() = "Hello, food!"