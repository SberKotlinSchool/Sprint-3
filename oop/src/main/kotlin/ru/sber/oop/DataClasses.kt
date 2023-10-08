package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        if (other == null || this::class != other::class) return false
        val obj = other as User
        return this.name == other.name && this.age == obj.age && this.city == obj.city
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
    val user2 = user1.copy(name = "Lex")

    user1.city = "Omsk"

    val user3 = user1.copy().let { it.city = "Tomsk" }

    println(user1.equals(user3))
}