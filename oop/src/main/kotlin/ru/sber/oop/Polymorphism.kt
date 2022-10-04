package ru.sber.oop

import kotlin.random.Random

/**
 * Polymorphism
Добавьте метод чтения по умолчанию для поля damageRoll, которое возвращает радномное число.
Реализуйте класс Player, имплементирующий интерфейс ru.sber.oop.Fightable
с дополнительным полем name (строка) и isBlessed. attack уменьшает здоровье оппоненту на damageRoll, если isBlessed = false,
и удвоенный damageRoll, если isBlessed = true.
Результат функции attack - количество урона, которое нанес объект класса Player.
Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable со строковыми полями name и description.
Логика функции attack, такая же как и в предыдущем пункте, только без учета флага isBlessed (которого у нас нет).
Реализуйте наследника класса Monster - класс Goblin. Переопределите в нем метод чтения damageRoll
(допустим, он в два раза меньше сгененрированного рандомного значения).
Добавьте в класс ru.sber.oop.Room поле типа Monster и инициализируйте его экземпляром класса Goblin.
Добавьте функцию-расширение к классу Monster, getSalutation() - которое выдает приветствтие монстра и вызовите ее в функции load() класса ru.sber.oop.Room.
 */
interface Fightable {
    val powerType: String
    var healthPoints: Int

    //Добавьте метод чтения по умолчанию для поля damageRoll, которое возвращает радномное число.
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

//Реализуйте класс Player, имплементирующий интерфейс ru.sber.oop.Fightable
class Player(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed: Boolean
    ): Fightable {

    //Результат функции attack - количество урона, которое нанес объект класса Player.
    override fun attack(opponent: Fightable): Int =
        when {
            isBlessed -> 2 * damageRoll
            else -> damageRoll
        }
}

//Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable со строковыми полями name и description.
abstract class Monster(
    name: String,
    description: String
    ): Fightable {

    override fun attack(opponent: Fightable): Int = damageRoll
}

//Реализуйте наследника класса Monster - класс Goblin. Переопределите в нем метод чтения damageRoll
class Goblin(override val powerType: String,
             override var healthPoints: Int
             ): Monster("Крюкохват", "Жадный гоблин") {

    override val damageRoll: Int
        get() = super.damageRoll/2
}

fun Monster.getSalutation(name: String): String = "Привет $name"





