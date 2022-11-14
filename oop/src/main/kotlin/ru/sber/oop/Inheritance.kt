package ru.sber.oop

open class Room(val name: String, val size: Int) {

    protected open val dangerLevel = 5

    var monster: Monster = Goblin()

    constructor(name: String) : this(name, 100)

    fun description() = "Room: $name"

    open fun load(): String = monster.getSalutation()
}

/**
 * Предпологаем, что от класса можно наследоваться, но наследникам нельзя переопределять метод load.
 * В противном случае, необходимости помечать метод модификатором final необязательно.
 */
open class TownSquare : Room(name = "Town Square", size = 1_000) {

    override val dangerLevel: Int = super.dangerLevel - 3

    final override fun load(): String = "Commonly found in the heart of a traditional town."
}
