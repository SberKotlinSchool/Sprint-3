package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, size = 100)

    protected open val dangerLevel = 5

    val monster: Monster = Goblin(
        name = "Azog",
        description = "Orc leader",
        powerType = "Superhuman strength, speed, and durability",
        healthPoints = 1000
    )

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare : Room(name = "Town Square", size = 1000) {
    override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load() = "Вы смотрите на строку для загрузки"
}


fun Monster.getSalutation(): String {
    return "Kod, toragid biriz"
}

//fun main() {
//    val room = Room("room")
//    println(room.monster.getSalutation())
//}