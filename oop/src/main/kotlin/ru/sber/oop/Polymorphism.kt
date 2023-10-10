package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int

    //1) Добавьте метод чтения по умолчанию для поля damageRoll, которое возвращает рандомное число.
    val damageRoll: Int
        get() = Random.nextInt(0, 10)

    fun attack(opponent: Fightable): Int
}


class Player(override val powerType: String,
             override var healthPoints: Int,
             private val name: String,
             private val isBlessed: Boolean) : Fightable {

    /**
     *  Метод attack уменьшает здоровье оппоненту на damageRoll, если isBlessed = false, и удвоенный damageRoll, если isBlessed = true. Результат функции attack -
     *  количество урона, которое нанес объект класса Player.
     */
    override fun attack(opponent: Fightable): Int {
        val damage: Int = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints = opponent.healthPoints - damage

        return damage
    }

    override fun toString(): String {
        return name
    }

}

//3) Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable со строковыми полями
//   name и description. Логика функции attack, такая же как и в предыдущем пункте, только без учета флага isBlessed (которого у нас нет).

open class Monster(override val powerType: String,
                   override var healthPoints: Int,
                   open val name: String,
                   open val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints = opponent.healthPoints - damage
        return damage
    }

    override fun toString(): String {
        return "$name($description)"
    }
}

//4) Реализуйте наследника класса Monster - класс Goblin. Переопределите в нем метод чтения damageRoll (допустим, он в два раза меньше сгененрированного
//   рандомного значения).
class Goblin(override val description: String) : Monster("Нож", 4, "Гоблин", description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

//6) Добавьте функцию-расширение к классу Monster, getSalutation() - которое выдает приветствтие монстра
//   и вызовите ее в функции load() класса ru.sber.oop.Room.
fun Monster.getSalutation(): String {
    return "Агрр!"
}

/**
 * Проверка методов через проигрывание боя.
 */
fun main() {

    val p1 = Player("меч", 10, "Рыцарь", true)
    val p2 = Player("огнестрел", 5, "Пехотинец", false)

    var winner = fight(p1, p2)

    val p3 = Monster("когти", 6, "Волк", "серый")
    winner = fight(winner, p3)

    val p4 = Goblin("страшный")
    winner = fight(winner, p4)
    /*
    log:
    Рыцарь(10) vs Пехотинец(5)
    Рыцарь(10) наносит урон 14 Пехотинец(-9)
    Победил Рыцарь
    Рыцарь(10) vs Волк(серый)(6)
    Рыцарь(10) наносит урон 0 Волк(серый)(6)
    Волк(серый)(6) наносит урон 4 Рыцарь(6)
    Рыцарь(6) наносит урон 18 Волк(серый)(-12)
    Победил Рыцарь
    Рыцарь(6) vs Гоблин(страшный)(4)
    Рыцарь(6) наносит урон 10 Гоблин(страшный)(-6)
    Победил Рыцарь
     */
}

private fun fight(p1: Fightable, p2: Fightable): Fightable {
    println("$p1(${p1.healthPoints}) vs ${p2}(${p2.healthPoints})")

    while (p1.healthPoints > 0 && p2.healthPoints > 0) {
        println("${p1}(${p1.healthPoints}) наносит урон ${p1.attack(p2)} ${p2}(${p2.healthPoints})")
        if (p2.healthPoints > 0) {
            println("${p2}(${p2.healthPoints}) наносит урон ${p2.attack(p1)} ${p1}(${p1.healthPoints})")
        }
    }

    val winner = if (p1.healthPoints > 0) p1 else p2
    println("Победил $winner")
    return winner
}



