package ru.sber.oop


//Формируем открытый класс для возможности наследования
open class Room(val name: String, val size: Int) {
    //устанавливаем protected, чтобы данной переменной мог пользоваться только наследуемый класс
    protected open val dangerLevel = 5
    fun description() = "Room: $name"

    //Вторичный конструктор с доп. параметром id (так как нельзя делать вторичный конструктор иденнтичным первичному)
    constructor(name: String, size: Int= 100, id: Int=1) : this(name,size) {
    }
    //Инициализация с отслеживанием вывода ошибки по части имени ()
    init {
        require(name.isNotBlank()) { "Имя отсутвует" }
    }

    val monster:  Monster = Goblin(name = "Григорий",
                                   description = "Крутой парень",
                                   powerType = "Сковородка",
                                   healthPoints = 100)
    //Функция расширения
    fun Monster.getSalutation() = println("Нападай ублюдок !!!")

    //Для доступа к функции из наследуемого класса так же присваиваем open
    open fun load(): String {
        monster.getSalutation()
        return "Nothing much to see here..."
    }

}

//TODO: create class TownSquare here...

//Наследуемы класс с заданными первичными данными
class TownSquare(name: String = "Town Square", size: Int = 1000) : Room(name, size) {

    //Переназначение функции load (закрытая для дальнейшего наследования)
    final override fun load(): String {
        return "Подождите...."
    }
    //Переназначенное значение
    override val dangerLevel: Int
        get() = super.dangerLevel - 3
}
