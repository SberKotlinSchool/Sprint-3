package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        return if (other is User) {
            (this.city ==  other.city)
                    && (this.age == other.age)
                    && (this.name == other.name)
        } else {
            false
        }
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Gustav")
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"

    if(user1 == user3) {
        println("Equals")
    }

}