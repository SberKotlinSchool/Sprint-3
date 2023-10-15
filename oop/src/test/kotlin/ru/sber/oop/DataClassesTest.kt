package ru.sber.oop

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class DataClassesTest {

    /**
     * 1) Создайте user2, изменив имя и используя функцию copy()
     */
    @Test
    fun `Создайте user2 изменив имя и используя функцию copy`() {
        val user1 = User("Alex", 13)

        val user2 = user1.copy(name = "Black")
        println("user2 = $user2")

        assertEquals("Black", user2.name)
    }


    /**
     * 2) Измените город user1 на 'Omsk' и создайте копию user1 - user3, только с городом 'Tomsk'.
     * Сравните user1 и user3, используя функцию equals().
     * Исправьте class User так, чтобы функция equals выдавала правильный ответ.
     */
    @Test
    fun `Измените город user1 на 'Omsk' и создайте копию user1`() {
        val user1 = User("Alex", 13)

        user1.city = "Omsk"
        val user3 = user1.copy()
        user3.city = "Tomsk"

        assertEquals("User(name=Alex, age=13)", user1.toString())
        assertEquals("User(name=Alex, age=13)", user3.toString())

        assertFalse(user1 == user3)

    }


}
