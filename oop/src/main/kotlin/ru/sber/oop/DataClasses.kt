package ru.sber.oop

data class User(val name: String, val age: Long, var city: String? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        else if (other == null) return false
        else if (other !is User) return false
        else if (name != other.name || age != other.age) return false
        else return true
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy("Bob")

    user1.city = "Omsk"
    val user3 = user1.copy().apply { city = "Томск" }

    println(user1.equals(user3))
}