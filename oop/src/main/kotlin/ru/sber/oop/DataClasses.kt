package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    /**
     * Метод для переопределения equals
     */
    override fun equals(other: Any?): Boolean {
        return (other is User)
                && this.name == other.name
                && this.age == other.age
                && this.city == other.city
    }

    /**
     * Метод для переопределения hashCode
     */
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }

}

//testDrive
//fun main() {
//    val user1 = User("Alex", 13)
//    //val user2 = user1.copy(name = "Petric")
//    user1.city = "Omsk"
//    val user3 = user1.copy()
//    user3.city = "Tomsk"
//    println(user1 == user3)
//}
