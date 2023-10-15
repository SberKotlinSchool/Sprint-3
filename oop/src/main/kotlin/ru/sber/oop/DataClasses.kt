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

    override fun hashCode(): Int = super.hashCode() * 31 + city.hashCode()

    override fun toString(): String {
        return if (this::city.isInitialized) {
            "User(name=$name, age=$age, city=$city)"
        } else {
            "User(name=$name, age=$age)"
        }
    }
}

fun main() {
    val user1 = User("Alex", 13)

    val user2 = user1.copy(name = "Piter")
    user1.city = "Omsk"

    val user3 = user1.copy().also { it.city = "Tomsk" }

    println(user1)
    println(user2)
    println(user3)
    println(user1 == user3)
}