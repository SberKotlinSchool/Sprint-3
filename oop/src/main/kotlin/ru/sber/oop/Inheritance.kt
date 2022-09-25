package ru.sber.oop

open class Room(val name: String, val size: Int, var monster: Monster? = null) {
    // Вторичный конструктор с размером 100 по умолчанию
    constructor(name: String) : this(name, 100)

    // Делаем доступной только для наследников
    // с помощью ключевого слова protected
    protected open val dangerLevel = 5

    fun description() = "Room: $name"

    // Добавлена возможность переопределения функции
    open fun load() =
        monster?.getSalutation() ?: "Nothing much to see here..."
}

open class TownSquare(name: String, size: Int) : Room(name, size) {
    // Конструктор со значениями по умолчанию
    constructor() : this("Town Square", 1000)

    // Переопределяем dangerLevel
    override val dangerLevel: Int
        get() = super.dangerLevel - 3

    // Запрещаем дальнейшее переопределение с помощью ключевого слова final
    final override fun load(): String = "Создан объект \"$name\" размером $size, dangerLevel: $dangerLevel"
}

class TownSquareChild(name: String, size: Int) : TownSquare(name, size) {
    // Если раскомментировать, то получим ошибку компиляции,
    // так как нельзя переопределять функцию, объявленную как final
    /*override fun load(): String {
        return super.load()
    }*/
}

fun main() {
    // Тестируем вторичный конструктор со значениями по умолчанию
    val townSquare = TownSquare()
    // Тестируем переопределённую функцию load()
    println(townSquare.load())

    val townSquareChild = TownSquareChild("TownSquareChild", 500)
    println(townSquareChild.load())

    // Если раскомментировать, то получим ошибку компиляции,
    // так как её использование доступно только для наследников
    /*println("dangerLevel: ${townSquareChild.dangerLevel}")*/

    // Проверяем вторичный конструктор
    val room = Room("Room")
    println("Создан объект \"${room.name}\" размером ${room.size}")
    println(room.description())
    println(room.load())

    // При наличии монстра функция load() выводит его приветствие
    room.monster = Goblin("Злобный гоблин", "Жуткий смех", 100)
    println(room.load())
}
