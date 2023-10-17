package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
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
        val path = Paths.get("io/logs")

        Files.walk(path).use { stream ->
            stream
                .filter(Files::isRegularFile)
                .forEach { currentPath ->
                    currentPath.useLines {
                        it.withIndex()
                            .filter { s -> s.value.contains(subString) }
                            .forEach { str ->
                                Files.writeString(
                                    Path.of("io/result.txt"),
                                    "${currentPath.fileName} : ${str.index + 1} : ${str.value}" + System.lineSeparator(),
                                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
                                )
                            }
                    }
                }
        }
    }
}

fun main() {
    Grep().find("22/Jan/2001:14:27:46")
}