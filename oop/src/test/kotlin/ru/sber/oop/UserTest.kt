package ru.sber.oop

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable


class UserTest{

    private val name = "Иванов Иван Иванович"
    private val age = 30L
    private val user = User(name, age)

    @Test
    fun testBase() {
        assertAll("user1",
            Executable { assertEquals(name, user.name) },
            Executable { assertEquals(age, user.age) }
        )
    }
}