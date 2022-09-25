package ru.sber.nio

import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Files.find
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Paths.*
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
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

        val path = get("io/logs")
        val resultFile = Paths.get("io/result.txt")

        Files.newOutputStream(resultFile).use { writer ->
            Files.walk(path).filter { p -> !p.isDirectory() }.forEach { file ->
                var count = 0;
                file.useLines { line ->
                    line.forEach { string ->
                        count++
                        if (string.contains(subString)) {
                            writer.write("${file.fileName} : $count : $string\n".toByteArray())
                        }
                    }
                }
                count = 0
            }
            writer.flush()
        }
    }
}

fun main() {
    Grep().find("54")
}

