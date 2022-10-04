package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (other !is User) return false
        return name.equals(other.name) && age.equals(other.age) && city.equals(other.city)
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Kirill")
    user1.city = "Omsk"
    val user3 = user1.copy().apply { city = "Tomsk" }

    if (user1.equals(user3)) {
        println("Объекты User1 и User3 равны")
    } else {
        println("Объекты User1 и User3 не равны")
    }
}