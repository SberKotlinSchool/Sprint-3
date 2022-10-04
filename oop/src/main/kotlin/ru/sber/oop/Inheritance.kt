package ru.sber.oop

open class Room(val name: String, val size: Int) {
    // 2: Добавил доступ толко наслоедникам через protected
    constructor(_name: String) : this(_name, 100)
    protected open val dangerLevel = 5

    open fun description() = "Room: $name"

    internal open fun load() = "Nothing much to see here..."

}
class TownSquare: Room ( "Town Square" ,1000) {
    override val dangerLevel = super.dangerLevel - 3

    // 1: Переопределил load()  и в качестве придумывания строку переопределил ещё и description
    override fun description() = "Town Square: $name"
    final override fun load(): String  = "This is a ${this.description()} "
}


