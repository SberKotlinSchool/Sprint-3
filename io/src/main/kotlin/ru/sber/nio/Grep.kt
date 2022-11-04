package ru.sber.nio

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.readLines

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep {
    /**
     * Метод должен выполнить поиск подстроки subString во всех файлах каталога logs.
     * Каталог logs размещен в данном проекте (io/logs) и внутри содержит другие каталоги.
     * Результатом работы метода должен быть файл в каталоге io(на том же уровне, что и каталог logs), с названием result.txt.
     * Формат содержимого файла result.txt следующий:
     * имя файла, в котором найдена подстрока : номер строки в файле : содержимое найденной строки
     * Результирующий файл должен содержать данные о найденной подстроке во всех файлах.
     * Пример для подстроки "22/Jan/2001:14:27:46":
     * 22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
     */
    fun find(subString: String) {

        val ls = System.lineSeparator()
        val inputDir = Path("io/logs")
        val outputFile = File("io/result.txt")

        Files.walk(inputDir)
            .filter { it.isRegularFile() }
            .map { Pair(it.fileName, it.readLines().withIndex()) }
            .forEach { pair -> pair.second.filter { it.value.contains(subString) }
                .forEach { outputFile.appendText("${pair.first} : ${it.index+1} : ${it.value} $ls", UTF_8) }
            }

    }
}
