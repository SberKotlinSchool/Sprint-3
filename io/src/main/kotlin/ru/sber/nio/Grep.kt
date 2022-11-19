package ru.sber.nio

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.useLines

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
        val pathResult = Path.of("io/result.txt")
        val pathLogs = Path.of("io/logs")
        Files.writeString(pathResult, "", StandardCharsets.UTF_8, StandardOpenOption.CREATE)
        Files.walk(pathLogs)
            .filter { !it.isDirectory() }
            .forEach { file ->
                file.useLines {
                    it.mapIndexed { index, str ->
                        if (str.contains(subString)) {
                            Files.writeString(
                                pathResult,
                                "${file.name} : $index : $str\n",
                                StandardCharsets.UTF_8,
                                StandardOpenOption.APPEND
                            )
                        }
                    }.toList()
                }
            }
    }
}

fun main() {
    Grep().find("192.168.1.1")
}