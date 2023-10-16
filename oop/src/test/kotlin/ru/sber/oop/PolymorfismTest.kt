package ru.sber.oop

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class PolymorfismTest {

    /**
     * 1) Добавьте метод чтения по умолчанию для поля damageRoll, которое возвращает радномное число.
     */
    @Test
    fun `1) Добавьте метод чтения по умолчанию для поля damageRoll которое возвращает радномное число`() {
        class TTT : Fightable {
            override val powerType: String
                get() = TODO("Not yet implemented")
            override var healthPoints: Int
                get() = TODO("Not yet implemented")
                set(value) {}

            override fun attack(opponent: Fightable): Int {
                TODO("Not yet implemented")
            }
        }

        val ttt = TTT()
        ttt.damageRoll

        assertTrue(ttt.damageRoll > -1)
    }


    /**
     * 2) Реализуйте класс Player, имплементирующий интерфейс ru.sber.oop.Fightable с дополнительным полем name (строка)
     * и isBlessed. attack уменьшает здоровье оппоненту на damageRoll, если isBlessed = false, и удвоенный damageRoll, если isBlessed = true. Результат функции attack -
     * количество урона, которое нанес объект класса Player.
     */
    @Test
    fun `2) Реализуйте класс Player имплементирующий интерфейс Fightable с дополнительным полем name`() {

        mockkObject(Random)
        every { Random.nextInt(any(), any()) } returns 5


        val player1 = Player("меч", 10, "Рыцарь", true)
        val player2 = Player("меч", 10, "Рыцарь", false)

        assertEquals(10, player1.attack(player2))
        assertEquals(5, player2.attack(player1))

        unmockkAll()
    }

    /**
     * 3) Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable со строковыми полями
     * name и description. Логика функции attack, такая же как и в предыдущем пункте, только без учета флага isBlessed (которого у нас нет).
     */
    @Test
    fun `3) Реализуйте абстрактный класс Monster`() {
        mockkObject(Random)
        every { Random.nextInt(any(), any()) } returns 5

        val player = Monster("когти", 6, "Волк", "серый")

        assertEquals(5, player.attack(player))

        unmockkAll()
    }

    /**
     * 4) Реализуйте наследника класса Monster - класс Goblin. Переопределите в нем метод чтения damageRoll (допустим, он в два раза меньше сгененрированного
     * рандомного значения).
     */
    @Test
    fun `4) Реализуйте наследника класса Monster - класс Goblin`() {
        mockkObject(Random)
        every { Random.nextInt(any(), any()) } returns 6

        val player = Goblin("страшный")
        assertEquals(3, player.attack(player))

        unmockkAll()
    }

    /**
     * 5) Добавьте в класс ru.sber.oop.Room поле типа Monster и инициализируйте его экземпляром класса Goblin.
     */
    @Test
    fun `5) Добавьте в класс Room поле типа Monster и инициализируйте его экземпляром класса Goblin`() {

        val r = Room("комната")
        assertEquals("Гоблин(особо уродливый)", r.monster.toString())
    }


    /**
     * 6) Добавьте функцию-расширение к классу Monster, getSalutation() - которое выдает приветствтие монстра
     * и вызовите ее в функции load() класса ru.sber.oop.Room.
     */
    @Test
    fun `6) Добавьте функцию-расширение к классу Monster getSalutation`(){

        val player = Monster("когти", 6, "Волк", "серый")
        assertEquals("Агрр!", player.getSalutation())

        val r = Room("комната")
        assertEquals("Агрр!", r.monster.getSalutation())

        assertEquals("Агрр!", r.load())
    }

}