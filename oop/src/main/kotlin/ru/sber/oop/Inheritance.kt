package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, size = 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    fun getDangerLevelTest() = dangerLevel

    //6) Добавьте функцию-расширение к классу Monster, getSalutation() - которое выдает приветствтие монстра
    //   и вызовите ее в функции load() класса ru.sber.oop.Room.
    open fun load() = monster.getSalutation()


    //5) Добавьте в класс ru.sber.oop.Room поле типа Monster и инициализируйте его экземпляром класса Goblin.
    val monster: Monster = Goblin("особо уродливый")
}

open class TownSquare : Room(name = "Town Square", size = 1000) {

    final override fun load(): String {
        return "some more load"
    }

    override val dangerLevel: Int
        get() = super.dangerLevel - 3


    fun getDanderLevel(): Int {
        return dangerLevel
    }
}