package ru.sber.oop

open class Room(val name: String, val size: Int) {
    constructor( name: String ) : this( name, 100 )

    open protected val dangerLevel = 5
    val monster: Monster = Goblin()
    fun description() = "Room: $name"

    open fun load() = monster.getSalutation()
}
fun Monster.getSalutation():String = "Hello, I am $name"
class TownSquare(val townName: String, val townSize: Int = 1000 ): Room( townName, townSize){

    override val dangerLevel = super.dangerLevel - 3
    final override fun load() = "Town $townName is building"
}
