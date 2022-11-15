package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean =
        (other is User) &&
            this.age == other.age &&
            this.name.equals(other.name) &&
            (if (this.city.isNotBlank() && other.city.isNotBlank()) this.city.equals(other.city) else false)
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Max", age = 18)
    user1.city = "Omsk"
    val user3 = user1.copy().apply { city = "Tomsk" }
    println(user1.equals(user3))
}