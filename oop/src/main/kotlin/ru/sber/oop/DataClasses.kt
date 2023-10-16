package ru.sber.oop

data class User(val name: String, val age: Long, var city: String) {

    // Функция equals переопределена для корректного сравнения объектов
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as User
        return name == other.name && age == other.age && city == other.city
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }
}

fun main() {
    val user1 = User("Alex", 13, "Omsk")
    val user2 = user1.copy(name = "John")
    val user3 = user1.copy(city = "Tomsk")

    // Сравнение user1 и user3
    val areEqual = user1 == user3

    println("User1: $user1")
    println("User2: $user2")
    println("User3: $user3")
    println("User1 equals User3: $areEqual")
}
