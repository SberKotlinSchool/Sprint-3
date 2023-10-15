package ru.sber.nio

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

/**
 * Реализовать простой аналог утилиты grep с использованием классов из пакета java.nio.
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
    fun find(subString: String, path: String = "io/logs", resultPath: String = "io/result.txt") {
        try {
            FileWriter(File(resultPath)).use { fileWriter ->
                Files.walk(Path.of(path)).use { files ->
                    files
                        .filter { Files.isRegularFile(it) && it.toString().endsWith(".log") }
                        .forEach { file ->
                            try {
                                Files.lines(file).use { lines ->
                                    lines
                                        .collect(Collectors.toList())
                                        .withIndex()
                                        .filter { it.value.contains(subString) }
                                        .forEach {
                                            val resultString = "${file.fileName} : ${it.index + 1} : ${it.value}\n"
                                            fileWriter.write(resultString)
                                            println(resultString)
                                        }
                                }
                            } catch (ex: IOException) {
                                println(ex.message)
                            }
                        }
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
    }
}

fun main() {
    Grep().find("22/Jan/2001:14:27:46")
}