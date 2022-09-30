package ru.sber.oop

data class User(val name: String, val age: Long, var city: String? = null)

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Alex2")

    user1.city = "Omsk"

    val user3 = user1.copy().also { it.city = "Tomsk" }

    println(user1.equals(user3))
}
