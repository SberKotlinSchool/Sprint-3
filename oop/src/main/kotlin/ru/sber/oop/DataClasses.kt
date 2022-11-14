package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    @Override
    override fun equals (other: Any?) : Boolean {
        if(other == null) return false
        if(other !is User) return false
        return try{
                (this.name == other.name) &&
                (this.age == other.age) &&
                (this.city == other.city)
        } catch (e : UninitializedPropertyAccessException) {
            false
        }
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Changed name")

    user1.city = "Omsk"
    val user3 = user1.copy().apply { city = "Tomsk" }
    println(user3.equals(user1))
}