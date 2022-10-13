package ru.sber.oop

import kotlin.random.Random

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    val monster: Monster = Goblin(
        "BigNail",
        "The fourth son of the fifth king",
        "Feet",
        Random.nextInt(10, 50))

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    constructor(name: String) : this(name, 100)
}

class TownSquare : Room(name = "Town Square", size = 1000) {

    override val dangerLevel = super.dangerLevel - 3

    override fun load() = "The square is the most popular and in the same time the most boring place in the city"
}
