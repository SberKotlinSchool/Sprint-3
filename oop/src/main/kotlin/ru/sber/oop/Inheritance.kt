package ru.sber.oop

/**
 * Inheritance

Создайте подкласс класса ru.sber.oop.Room - TownSquare c именем "Town Square" и размером 1000.
Переопределите в новом классе функцию load() (придумайте строку для загрузки).
Переопределите dangerLevel в TownSquare, так чтобы сделать уровень угрозы на 3 пункта меньше среднего.
В классе ru.sber.oop.Room предоставить доступ к этой переменной только для наследников.
Запретите возможность переопределения функции load() в классе TownSquare.
Создайте в классе ru.sber.oop.Room вторичный конструктор, который бы инициализировал имя и задавал размер по умолчанию 100.
 */

open class Room(val name: String,
                val size: Int,
                private val monster: Monster = Goblin("Школота",100)) {

    constructor(name: String) : this("Room",100)

    protected open val dangerLevel = 5
    fun description() = "Room: $name"
    open fun load() = monster.getSalutation(name)
}

open class TownSquare(): Room("Town Square", 1000) {
    //Запретите возможность переопределения функции load() в классе TownSquare.
    final override fun load(): String = "Valera"

    override val dangerLevel: Int
        get() = super.dangerLevel - 3
}





