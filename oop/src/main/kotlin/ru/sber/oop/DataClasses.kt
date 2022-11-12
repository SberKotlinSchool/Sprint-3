package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is User)
            return false
        if (city == null || other.city == null)
            return false
        return name == other.name && age == other.age && city == other.city
    }

    override fun hashCode(): Int =
        (name.hashCode() * 31 + age.toInt()) * 31 + (if (city != null) city.hashCode() else 0)

}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Nick")
    println("user2.name = ${user2.name}")
    user1.city = "Omsk"
    println("user1.city = ${user1.city}")
    val user3 = user1.copy()
    user3.city = "Tomsk"
    println("user1.equals(user3) = ${user1.equals(user3)}")
}