package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        return if (other !is User) false
        else if (this ::city.isInitialized && other ::city.isInitialized)
            this.city == other.city && this.name === other.name && this.age == other.age
        else if (!this ::city.isInitialized && !other ::city.isInitialized)
            this.name === other.name && this.age == other.age
        else false
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Max") //Копирование обычное с изменением имени
    user1.city = "Omsk" //Изменяем значение после копирования
    val user3 = user1.copy().apply { city = "Tomsk" } //Копирование с изменением значения внутри
    println(user1.equals(user3)) //Изменённый equals с учётом значения city
}
