package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    //Исправьте class User так, чтобы функция equals выдавала правильный ответ.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

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
    val user1 = User("Alex", 13)

    val user2 = user1.copy(name = "Vasya")

    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"
    println(user1.equals(user3))
}