package ru.sber.oop

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InheritanceTest {


    /**
     * 1) Создайте подкласс класса ru.sber.oop.Room - TownSquare c именем "Town Square" и размером 1000. Переопределите в новом
     * классе функцию load() (придумайте строку для загрузки).
     */
    @Test
    fun `1) Создайте подкласс класса`() {

        val room = Room("комната")
        assertEquals("Агрр!", room.load())

        val ts = TownSquare()
        assertEquals("some more load", ts.load())

    }

    /**
     * 2) Переопределите dangerLevel в TownSquare, так чтобы сделать уровень угрозы на 3 пункта меньше среднего.
     * В классе ru.sber.oop.Room предоставить доступ к этой переменной только для наследников.
     */
    @Test
    fun `2) Переопределите dangerLevel в TownSquare`() {

        val room = Room("комната")
        assertEquals(5, room.getDangerLevelTest())

        val ts = TownSquare()
        println("dangerLevel: ${ts.getDanderLevel()}")//2
        assertEquals(2, ts.getDanderLevel())

    }

    /**
     * 3) Запретите возможность переопределения функции load() в классе TownSquare.
     */
    @Test
    fun `3) Запретите возможность переопределения функции load() в классе TownSquare`() {
        class TTT : TownSquare() {
            // override fun load(){} //Not compile.  'load' in 'TownSquare' is final and cannot be overridden
        }
    }


    /**
     * 4) Создайте в классе ru.sber.oop.Room вторичный конструктор, который бы инициализировал имя и задавал размер по умолчанию 100.
     */
    @Test
    fun `4) Создайте в классе Room вторичный конструктор который бы`(){
        val room1 = Room("комната", 200)
        val room2 = Room("комната")

        assertEquals(200, room1.size)
        assertEquals(100, room2.size)
    }


}