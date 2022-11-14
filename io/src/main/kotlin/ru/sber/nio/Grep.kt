package ru.sber.nio

import java.nio.file.*
import kotlin.io.path.*
import kotlin.streams.toList

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
        val rootDir = "io"
        val startPath = Paths.get("$rootDir/logs/")
        val resultPath = Paths.get("$rootDir/result.txt")

        val foundStrings = Files.walk(startPath)
            .filter { it.toString().endsWith(".log") }
            .flatMap { file ->
                file.readLines()
                    .filter { line -> line.contains(subString) }
                    .mapIndexed { index, line ->
                        "${file.fileName} : ${index + 1} : $line"
                    }
                    .stream()
            }
            .toList()
        Files.write(resultPath, foundStrings)
    }
}