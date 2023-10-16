package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    /**
     * Переопределяем equals, чтобы учитывать все поля
     */
    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return name == other.name && age == other.age && city == other.city
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return name.hashCode() + age.hashCode() + city.hashCode()
    }
}
