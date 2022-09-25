package ru.sber.oop

import ru.sber.oop.extensions.getSalutation

open class Room(val name: String, val size: Int) {
    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

    val monster: Monster = Goblin("Super Attack",15,"Gretchin","Goblin's Mother")
}

open class TownSquare : Room("Town Square", 1000) {

    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "Some string while loading..."
}

