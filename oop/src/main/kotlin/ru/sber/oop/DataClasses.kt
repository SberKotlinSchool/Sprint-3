package ru.sber.oop


data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is User)
            return false
        return name == other.name &&
                city == other.city &&
                age == other.age
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Mikhail")

    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"

    print(user1 == user3)
}