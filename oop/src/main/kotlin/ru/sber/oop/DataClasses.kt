package ru.sber.oop

data class User(val name: String, val age: Long, val city: String)

fun main() {
    val user1 = User("Alex", 13, "Omsk")
    val user2 = user1.copy(name = "Konstantin")
    val user3 = user1.copy(city = "Tomsk")
    println("user1.equals(user3) = ${user1 == user3}")

}