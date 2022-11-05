package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    constructor(name: String, age: Long, _city: String): this(name, age){
        city = _city
    }
}

fun main() {
    val user1 = User("Alex", 13, "Omsk")
    //TODO: user2 = ...
    val user2 = user1.copy("James")
    //TODO: user3 = ...
    val user3 = User(user1.name, "${user1.age}".toLong(), "Tomsk")
    println(user1 == user3)

}
