package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.stream.Stream
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.readLines
import kotlin.io.path.writeLines
import kotlin.streams.toList

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep {
    private val folder: Path = Paths.get("logs")
    private val resultFile: Path = Paths.get("result.txt")

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
        val fileFilter: (t: Path, u: BasicFileAttributes) -> Boolean = { f, _ -> !f.isDirectory() }
        val strings = Files.find(folder, 10, fileFilter)
            .flatMap { grepFile(it, subString) }
            .toList()

        resultFile.writeLines(strings)
    }

    private fun grepFile(file: Path, subString: String): Stream<String> {

        val lines = file.readLines()
        return lines
            .mapIndexed { index, line -> "${file.name} : ${index + 1} : $line" }
            .filter { line -> subString in line }
            .stream()
    }
}