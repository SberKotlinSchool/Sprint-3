package ru.sber.oop

open class Room(val name: String, val size: Int) {

    private val monster: Monster = Goblin()

    constructor() : this(name = "Room", size = 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare : Room(name = "Town Square", size = 1000) {

    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "Town Square is loading!"

}

fun Monster.getSalutation() = "Kill! Crush! Eat!"