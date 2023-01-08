package ru.sber.oop

data class User(val name: String, val age: Long, /*var city: String = ""*/) {
    // Была еще идея перенсти это поле в конструктор, но затем отказался
    // Я так понимаю, идея была в том, чтобы не менять существующий код
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

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }
}

fun main() {
    val user1 = User("Alex", 13)
    // 1.
    val user2 = user1.copy(name = "Olex")
    // 2.
    user1.city = "Omsk"
    val user3 = user1.copy().apply { city = "Tomsk" }
    println(user1 == user3)
}