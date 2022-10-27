package ru.sber.oop

import kotlin.random.Random

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5
    val monster: Monster = Goblin(
        "Strength",
        Random.Default.nextInt(1, Int.MAX_VALUE),
        "Goblin",
        "Ugly"
    )

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    fun Monster.getSalutation() = "Hi, buddy!"
}

class TownSquare : Room(name = "Town Square", size = 1000) {
    override val dangerLevel = super.dangerLevel - 3

    final override fun load(): String = "Loading..."


}
