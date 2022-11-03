package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertTrue

internal class ScannerTest {

    @BeforeEach
    fun setUp() {mockkObject(Random)} // Формируем пустышку для Random

    //Очищаем список mockk
    @AfterEach
    fun tearDown() {unmockkAll()}


    //Вызов ошибки ScanTimeoutException
    @Test
    fun getScanData_ScanTimeoutException_Test() {
        val best_timers = Scanner.SCAN_TIMEOUT_THRESHOLD + 1L // Формируем изменённое стартовое значение для определения ошибки
        every { Random.nextLong(any(),any()) } returns best_timers //Приравниваем рандомному значению результат больше фиксированного
        assertThrows<ScanTimeoutException> { Scanner.getScanData() }//Запускаем функцию c отловом ошибки
    }

    //Вызов варианта без ошибки ScanTimeoutException
    @Test
    fun getScanDataelse_Test() {
        val best_timers = Scanner.SCAN_TIMEOUT_THRESHOLD - 300L // Формируем изменённое стартовое значение для определения ошибки
        every { Random.nextLong(any(),any()) } returns best_timers //Приравниваем рандомному значению результат меньше фиксированного
        val result = Scanner.getScanData() //Запускаем функцию и получаем результат
        verify { Random.nextLong(any(),any()) } //Запускалась ли функция Random в начале
        verify { Random.nextBytes(100) } //Отработала ли функция nextBytes
        assertTrue { result.size == 100 } //Размер в байтах совпадает с заявленным
    }
}