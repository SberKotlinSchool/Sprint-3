package ru.sber.oop

data class User(val name: String, val age: Long, var city: String = ""){
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Alexey")
    user1.city = "Omsk"
    val user3 = user1.copy(city = "Tomsk" )
    println(user3 == user1 )
}