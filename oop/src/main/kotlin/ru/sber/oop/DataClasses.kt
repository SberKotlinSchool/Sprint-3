package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        return if (other is User) {
            this.name == other.name && this.age == age && this.city == other.city
        }else {
            false
        }
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
    val user2 = user1.copy(name = "Anton")
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"
}