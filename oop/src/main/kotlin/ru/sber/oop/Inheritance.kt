package ru.sber.oop

/*
    Inheritance
    1) Создайте подкласс класса ru.sber.oop.Room - TownSquare c именем "Town Square" и размером 1000. Переопределите в новом
    классе функцию load() (придумайте строку для загрузки).
    2) Переопределите dangerLevel в TownSquare, так чтобы сделать уровень угрозы на 3 пункта меньше среднего.
    В классе ru.sber.oop.Room предоставить доступ к этой переменной только для наследников.
    3) Запретите возможность переопределения функции load() в классе TownSquare.
    4) Создайте в классе ru.sber.oop.Room вторичный конструктор, который бы инициализировал имя и задавал размер по умолчанию 100.
 */
open class Room(val name: String, val size: Int) {

    constructor() : this("Room", 100)

    //Добавьте в класс ru.sber.oop.Room поле типа Monster и инициализируйте его экземпляром класса Goblin.
    private var monster: Monster = Goblin("BigBoss", 1000, "Goblin 1", "Main Goblin")

    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()

}

open class TownSquare() : Room("Town Square", 1000) {

    override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load(): String = "load in Town Square"
}

/*
    6) Добавьте функцию-расширение к классу Monster, getSalutation() - которое выдает приветствтие монстра
       и вызовите ее в функции load() класса ru.sber.oop.Room.
 */
fun Monster.getSalutation() = "Hello! I'm Monster!"