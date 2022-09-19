package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is User) return false
        return eqFields(other)
    }
}

fun User.eqFields(other: User) : Boolean {
    val (name1, age1) = this
    val (name2, age2) = other
    return name1.equals(name2) && age1.equals(age2) && this.city.equals(other.city)
}

fun main() {
    val user1 = User("Alex", 13)
    println("user1=$user1")
    val user2 = user1.copy(name="Alexa")
    println("user2=$user2")
    user1.city="Omsk"
    val user3=user1.copy()
    user3.city="Tomsk"
    println("user3${if (user1.equals(user3)) " " else " not "}equals user1")
}