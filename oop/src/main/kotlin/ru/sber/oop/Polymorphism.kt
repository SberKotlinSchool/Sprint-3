package ru.sber.oop

import kotlin.random.Random

/*
    Polymorphism
    1) Добавьте метод чтения по умолчанию для поля damageRoll, которое возвращает радномное число.
    2) Реализуйте класс Player, имплементирующий интерфейс ru.sber.oop.Fightable с дополнительным полем name (строка)
       и isBlessed. attack уменьшает здоровье оппоненту на damageRoll, если isBlessed = false, и удвоенный damageRoll, если isBlessed = true. Результат функции attack -
       количество урона, которое нанес объект класса Player.

    3) Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable со строковыми полями
       name и description. Логика функции attack, такая же как и в предыдущем пункте, только без учета флага isBlessed (которого у нас нет).
    4) Реализуйте наследника класса Monster - класс Goblin. Переопределите в нем метод чтения damageRoll (допустим, он в два раза меньше сгененрированного
       рандомного значения).
    5) Добавьте в класс ru.sber.oop.Room поле типа Monster и инициализируйте его экземпляром класса Goblin.
    6) Добавьте функцию-расширение к классу Monster, getSalutation() - которое выдает приветствтие монстра
       и вызовите ее в функции load() класса ru.sber.oop.Room.
 */

interface Fightable {
    val powerType: String
        get() = ""
    var healthPoints: Int
        get() = 0
        set(value) {}

    //Добавьте метод чтения по умолчанию для поля damageRoll, которое возвращает радномное число.
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

/*
    2) Реализуйте класс Player, имплементирующий интерфейс ru.sber.oop.Fightable с дополнительным полем name (строка)
       и isBlessed. attack уменьшает здоровье оппоненту на damageRoll, если isBlessed = false,
       и удвоенный damageRoll, если isBlessed = true.
       Результат функции attack - количество урона, которое нанес объект класса Player.
 */
class Player(val name: String, val isBlessed: Boolean) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val totalDamage = damageRoll * if (isBlessed) 2 else 1
        opponent.healthPoints -= totalDamage

        return totalDamage
    }
}

/*
        Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable
        со строковыми полями name и description.
        Логика функции attack, такая же как и в предыдущем пункте,
        только без учета флага isBlessed (которого у нас нет).
 */
abstract class Monster(val name: String, val description: String) : Fightable {

    override fun attack(opponent: Fightable): Int {

        opponent.healthPoints -= damageRoll

        return damageRoll
    }
}

/*
    Реализуйте наследника класса Monster - класс Goblin.
    Переопределите в нем метод чтения damageRoll (допустим, он в два раза меньше сгененрированного рандомного значения).
 */
class Goblin(name: String, description: String) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll / 2
}

