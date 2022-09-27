package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is User) return false
        if (name != other.name || age != other.age || city != other.city) return false
        return true
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Mike")
    user1.city = "Omsk"
    val user3 = user1.copy().apply { city = "Tomsk" }
}