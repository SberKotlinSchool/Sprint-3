package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (age != other.age) return false
        if (city != other.city) return false

        return true
    }
}

fun main() {
    val user1 = User("Alex", 13).apply {
        city = "Omsk"
    }
    val user2 = user1.copy(name = "Peter")
    val user3 = user1.copy().apply {
        city = "Tomsk"
    }
}