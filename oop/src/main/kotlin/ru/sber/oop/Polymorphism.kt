package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int get() = Random.nextInt(100)

    fun attack(opponent: Fightable): Int
}

//TODO: create class Player, Monster, Goblin here...
// attack уменьшает здоровье оппоненту на damageRoll, если isBlessed = false,
// и удвоенный damageRoll, если isBlessed = true.
// Результат функции attack - количество урона, которое нанес объект класса Player.
class Player(
    override val powerType : String,
    override var healthPoints: Int,
    val name: String,
    val isBlessed : Boolean
) : Fightable {

    override fun attack( fightable  : Fightable): Int {
        if(isBlessed) {
            fightable.healthPoints -= damageRoll * 2
            return damageRoll*2
        }
        else {
            fightable.healthPoints -= damageRoll
            return damageRoll
        }
    }

}

//Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable
//со строковыми полями name и description. Логика функции attack,
//такая же как и в предыдущем пункте, только без учета флага isBlessed (которого у нас нет).

abstract class Monster(
    val name: String,
    val description : String
) : Fightable {

    override fun attack( fightable  : Fightable): Int {

            fightable.healthPoints -= damageRoll
            return damageRoll
        }
    }

//Реализуйте наследника класса Monster - класс Goblin. Переопределите в нем метод
//чтения damageRoll (допустим, он в два раза меньше сгененрированного рандомного значения).

class Goblin(
    override val powerType : String,
    override var healthPoints: Int,
    name: String,
    description: String
) : Monster(name, description) {
    override val damageRoll: Int
        get() = super.damageRoll /2
}

fun Monster.getSalution(): String = "Бууууу"





