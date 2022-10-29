package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int get() = Random((Math.random() * 100).toInt()).nextInt(50, 200)

    fun attack(opponent: Fightable): Int
}

class Player(override val powerType: String,
             override var healthPoints: Int,
             val name: String,
             private val isBlessed: Boolean) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = if (isBlessed) {damageRoll * 2} else {damageRoll}
        opponent.healthPoints -= damage
        return damage
    }
}

abstract class Monster(
    override val powerType: String,
    override var healthPoints: Int,
    val name: String,
    val description: String) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage = damageRoll
        opponent.healthPoints -= damage
        return damage
    }
}

class Goblin(powerType: String, healthPoints: Int, name: String, description: String) :
    Monster(powerType, healthPoints, name, description) {

    override val damageRoll: Int
        get() = super.damageRoll/2
}

/*
   Можно добавить тест на баланс (подобрать параметры, чтобы количество побед/поражений было одинаковым)
   Сильно влияет на баланс не только значение интервала начального health, но и seed рандомайзера
   С данными параметрами Пендальф иногда умирает
*/
fun main() {

    val player = Player("magic", Random(89543).nextInt(1000, 2000), "Pendalf", Random((Math.random()*20).toInt()).nextBoolean())
    val goblin = Goblin("fire", Random(432).nextInt(300, 1000), "Goblin", "Aaaarhhhh!!!")
    val troll = Goblin("frozen", Random(299).nextInt(700, 1200), "Troll", "BuuuGaaaGaaa!!!")

    println("One time ago Pendalf the Gray was walking over the forest. But Sarumyan was prepared a siege...")
    println("${goblin.description} - is a ${goblin.name}!")
    println("${troll.description} - is a ${troll.name}!!!")
    println("The battle is coming!")

    var goblinIsAlive = true
    var trollIsAlive = true
    printHealth(player, goblin, troll, goblinIsAlive, trollIsAlive)

    while(true) {
        if (goblinIsAlive) {
            if (isPlayerDeadAfterMonsterAttack(player, goblin)) break
        }
        if (trollIsAlive) {
            if (isPlayerDeadAfterMonsterAttack(player, troll)) break
        }
        // если враги живы, рандомно атаковать одного из них
        if (goblinIsAlive and trollIsAlive) {
            if (Random((Math.random()*35).toInt()).nextBoolean()) {
                if (isMonsterDeadAfterPlayerAttack(player, goblin)) {
                    goblinIsAlive = false
                }
            } else {
                if (isMonsterDeadAfterPlayerAttack(player, troll)) {
                    trollIsAlive = false
                }
            }
        } else {
            // иначе если один из них склеился, атаковать другого
            if (goblinIsAlive) {
                if (isMonsterDeadAfterPlayerAttack(player, goblin)) {
                    goblinIsAlive = false
                }
            } else {
                if (isMonsterDeadAfterPlayerAttack(player, troll)) {
                    trollIsAlive = false
                }
            }
        }
        if (!goblinIsAlive and !trollIsAlive) {
            println("------ ${player.name} escaped the death trap!")
            break
        } else {
            printHealth(player, goblin, troll, goblinIsAlive, trollIsAlive)
        }
    }
    println("Result: ${player.name} ${printFightable(player)}, ${goblin.name} ${printFightable(goblin)}, ${troll.name} ${printFightable(troll)}")
}

private fun printHealth(player: Player, goblin: Goblin, troll: Goblin, goblinIsAlive: Boolean, trollIsAlive: Boolean) {
    print("-- ${player.name} ${printFightable(player)}")
    if (goblinIsAlive) {
        print(", ${goblin.name} ${printFightable(goblin)}")
    }
    if (trollIsAlive) {
        print(", ${troll.name} ${printFightable(troll)}")
    }
    println()
}

private fun printFightable(f: Fightable) : String{
    return "health: ${f.healthPoints}"
}

private fun isPlayerDeadAfterMonsterAttack(player: Player, goblin: Goblin): Boolean {
    printMonsterAttack(player, goblin)
    if (isDead(player)) {
        printDeadPlayer(player)
        return true
    }
    return false
}

private fun isMonsterDeadAfterPlayerAttack(player: Player, goblin: Goblin): Boolean {
    printPlayerAttack(player, goblin)
    if (isDead(goblin)) {
        printDeadMonster(goblin)
        return true
    }
    return false
}

private fun printMonsterAttack(player: Player, monster: Monster) {
    println("${player.name} was attacked by ${monster.name}, damage ${monster.attack(player)}, health: ${player.healthPoints}")
}
private fun printPlayerAttack(player: Player, monster: Monster) {
    println("${monster.name} was attacked by ${player.name}, damage ${player.attack(monster)}, health: ${monster.healthPoints}")
}


private fun isDead(fightable: Fightable) : Boolean {
    return (fightable.healthPoints <= 0)
}

private fun printDeadPlayer(player: Player) {
    println("------ ${player.name} is dead... Sarumyan celebrating victory!")
}

private fun printDeadMonster(monster: Monster) {
    println("---- ${monster.name} is dead! Power is in the truth!")
}