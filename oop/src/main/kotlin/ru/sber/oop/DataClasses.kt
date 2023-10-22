package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is User) return false

        if (this.name != other.name) return false
        if (this.age != other.age) return false

        if (!this::city.isInitialized && !other::city.isInitialized) {
            return true
        } else if (this::city.isInitialized && other::city.isInitialized) {
            if (this.city != other.city) return false
        } else {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Bob")
    user1.city = "Omsk"
    val user3 = user1.copy().apply { city = "Tomsk" }
    println(user1.equals(user3)) // false
}