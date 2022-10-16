package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (other !is User) return false
        return (name != other.name && age != other.age && city != other.city)
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy("Mikhail")

    user1.city = "Orenburg"
    val user3 = user1.copy()
    user3.city = "Moscow"

    println(user1.equals(user3))
}