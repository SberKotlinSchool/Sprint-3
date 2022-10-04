package ru.sber.oop

data class User(val name: String, val age: Long, var city: String? = null) {

}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy("Miha")
    user1.city = "Nant"
    val user3 = user1.copy()
    user3.city = "Sent"
    println(user1.equals(user3))
}