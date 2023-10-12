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
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Alex2")
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.also { it.city = "Tomsk" }
    println(user1.equals(user3))
}