package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String): this(name, 100)
    protected open val dangerLevel = 5
    fun description() = "Room: $name"
    open fun load() = "Nothing much to see here. But... ${monsterGoblin.getSalutation()}"
    private val monsterGoblin: Monster = Goblin("Goblinus", "Ugly creature")
}

open class TownSquare: Room("Town Square", 1000) {
    final override fun load() = "Другая строка для загрузки"
    override val dangerLevel = super.dangerLevel - 3
}

fun Monster.getSalutation() = "Whoa!"
